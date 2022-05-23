package br.com.safi.services.calculators;

import br.com.safi.controller.dto.DarfDto;
import br.com.safi.models.Transaction;

import br.com.safi.services.WalletCurrencyService;
import br.com.safi.services.interfaces.ICalcTax;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class DarfCalculator implements ICalcTax {

    private static final String BUY = "COMPRA";
    private static final List<String> MONTHS = List.of("Janeiro", "fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");
    private static final int TAX_VALUE = 35000;
    private static final double PERCENT_TAX = 0.15;

    @Autowired
    private WalletCurrencyService walletCurrencyService;

    @Override
    public List<DarfDto> calcTax(List<Transaction> transactionList, int year) {
        List<DarfDto> darfs = new ArrayList<>();
        var listaTransacaoAnoAtual = transactionList.stream().filter(x -> x.getTransactionDate().getYear() == year).collect(Collectors.toList());
        List<CurrencyHistory> currencyHistoriesList = new ArrayList<>();
        transactionList.removeAll(listaTransacaoAnoAtual);
        var transacoesAnteriores = transactionList
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency)).entrySet();

        for (var transactionMap : transacoesAnteriores) {
            Double currentQuantity = 0.0;
            Double currentValueTotal = 0.0;
            for (Transaction transaction : transactionMap.getValue()) {
                if (BUY.equals(transaction.getTransactionStatus().getStatus())) {
                    currentQuantity += transaction.getCurrencyQuantity();
                    currentValueTotal += transaction.getAmountInvested();
                } else {
                    if (currentQuantity >= transaction.getCurrencyQuantity()) {
                        currentValueTotal -= (transaction.getCurrencyQuantity() / currentQuantity) * currentValueTotal;
                        currentQuantity -= transaction.getCurrencyQuantity();
                    } else {
                        throw new IllegalArgumentException("Valor maior do que o disponível para venda. ID:" +
                                transaction.getId() + ", DATA: "
                                + transaction.getTransactionDate() + ", MOEDA: "
                                + transaction.getCurrency().getName() + ", VALOR: "
                                + transaction.getAmountInvested() + ", QUANTIDADE: "
                                + transaction.getCurrencyQuantity());
                    }
                }
            }

            currencyHistoriesList.add(new CurrencyHistory(transactionMap.getKey().getName(), currentQuantity, currentValueTotal));
        }

        for (int month = 0; month < MONTHS.size(); month++) {
            int monthValue = month + 1;

            List<Transaction> monthTransaction = listaTransacaoAnoAtual
                    .stream()
                    .filter(x -> x.getTransactionDate()
                            .getMonthValue() == monthValue).collect(Collectors.toList());

            var mapTransactionList = monthTransaction
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCurrency)).entrySet();

            Double volBuy = 0.0;
            Double volSell = 0.0;
            double balancer = 0.0;
            boolean hasDebit = false;
            for (var mapTransactions : mapTransactionList) {
                var transactions = mapTransactions.getValue();
                double currentValueTotal = 0.0;
                for (Transaction transaction : transactions) {
                    var currencyHistory = currencyHistoriesList
                            .stream().filter(x -> x.getName().equals(transaction.getCurrency().getName())).findFirst().orElse(null);

                    if (currencyHistory != null) {
                        if (BUY.equals(transaction.getTransactionStatus().getStatus())) {
                            currencyHistory.setQuantity(currencyHistory.getQuantity() + transaction.getCurrencyQuantity());
                            currencyHistory.setInvestedValue(currencyHistory.getInvestedValue() + transaction.getAmountInvested());
                            volBuy += transaction.getAmountInvested();
                        } else {
                            if (currencyHistory.getQuantity() >= transaction.getCurrencyQuantity()) {
                                balancer += transaction.getAmountInvested() - ((transaction.getCurrencyQuantity() / currencyHistory.getQuantity()) * currentValueTotal);
                                currencyHistory.setInvestedValue(currencyHistory.getInvestedValue() - (transaction.getCurrencyQuantity() / currencyHistory.getQuantity()) * currentValueTotal);
                                currencyHistory.setQuantity(currencyHistory.getQuantity() - transaction.getCurrencyQuantity());
                                volSell += transaction.getAmountInvested();
                            } else {
                                throw new IllegalArgumentException("Valor maior do que o disponível para venda.");
                            }
                        }
                    }
                }
            }

            var debitValue = 0.0;
            if (volSell >= TAX_VALUE && balancer > 0) {
                hasDebit = true;
                debitValue = balancer * PERCENT_TAX;
            }

            String expirationDate = LocalDateTime.now().plusMonths(1).truncatedTo(ChronoUnit.SECONDS).toString();

            darfs.add(DarfDto.builder()
                    .dataExpiracao(expirationDate)
                    .valorDebito(debitValue)
                    .volumeCompra(volBuy)
                    .volumeVenda(volSell)
                    .balanco(balancer).temDebito(hasDebit).mes(MONTHS.get(monthValue - 1)).build());
        }

        return darfs;
    }
}
