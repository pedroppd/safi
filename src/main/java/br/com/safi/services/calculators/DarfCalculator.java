package br.com.safi.services.calculators;

import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.services.interfaces.ICalcTax;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DarfCalculator implements ICalcTax {

    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

    @Override
    public void calcTax(List<Transaction> transactionList) {

        for (int month = 1; month < 12; month++) {
            int monthValue = month;
            List<Transaction> monthTransaction = transactionList
                    .stream()
                    .filter(x -> x.getTransactionDate()
                            .getMonthValue() == monthValue).collect(Collectors.toList());

            var mapTransactionList = monthTransaction
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCurrency)).entrySet();

            Double volumeCompra = 0.0;
            Double volumeVenda = 0.0;
            Double balancer = 0.0;
            boolean hasDebit = false;
            for (var mapTransactions : mapTransactionList) {
                var transactions = mapTransactions.getValue();
                //TODO: Pegar todas as transações de uma determinada moeda. DONE
                //TODO: Ordenar por data ascendente. DONE
                //TODO: Criar variável de quantidade atual e valor total atual.
                //TODO: Em ordem de compra somar a quantidade atual e o valor total atual.
                //TODO: Criar variável 'balancer'
                //TODO: Em ordem de venda atualizar variável balancer.
                //TODO: Atualizar a variável quantidade atual = (Quantidade atual - Quantidade vendida) e
                //TODO: valor total atual = (valor total atual - (Quantidade Vendida * Valor total atual))
                /*"hasDebit": true, //balance > 0 && volumeTradedSell > 35000
                        "volumeTradedBuy": 40000,
                        "volumeTradedSell": 20000,
                        "debitValue": 15.000,00,
                        "expirationDate": "20/06/2022",
                        "balance": -1.000,00,
                        "month": 'Maio'	*/
                Double quantidadeAtual = 0.0;
                Double valorTotalAtual = 0.0;
                for (Transaction transaction : transactions) {
                    if (BUY.equals(transaction.getTransactionStatus().getStatus())) {
                        quantidadeAtual += transaction.getCurrencyQuantity();
                        valorTotalAtual += transaction.getCurrencyValue();
                        volumeCompra += transaction.getCurrencyValue();
                    } else {
                        if (quantidadeAtual >= transaction.getCurrencyQuantity()) {
                            balancer += transaction.getCurrencyValue()-((transaction.getCurrencyQuantity()/quantidadeAtual) * valorTotalAtual);
                            valorTotalAtual -= (transaction.getCurrencyQuantity()/quantidadeAtual) * valorTotalAtual;
                            quantidadeAtual -= transaction.getCurrencyQuantity();
                            volumeVenda += transaction.getCurrencyValue();
                        } else {
                            throw new IllegalArgumentException("Valor maior do que o disponível para venda.");
                        }
                    }
                }
            }
            var debitValue = 0.0;
            if(volumeVenda >= 35000 && balancer > 0) {
                hasDebit = true;
                debitValue = balancer * 0.15;
            }
            var oHasDebit = hasDebit;
            var oVolumeCompra = volumeCompra;
            var oVolumeVenda = volumeVenda;
            var oDebitValue = debitValue;
            var expirationDate = LocalDateTime.now().plusMonths(1);
            var month2 = "jan";
            var oBalancer = balancer;
        }
    }

    private BigDecimal makeTaxCalc(BigDecimal totalSell, BigDecimal quantitySell, BigDecimal quantityBuy, BigDecimal totalBuy) {
        var result = totalSell.subtract(quantitySell.divide(quantityBuy, RoundingMode.CEILING).multiply(totalBuy));
        return result.setScale(2, RoundingMode.CEILING);
    }

    private boolean needToPayTax(BigDecimal totalSellAmount) {
        return totalSellAmount.compareTo(new BigDecimal(35000)) > 0;
    }


    private List<Transaction> getTotalSellTransaction(List<Transaction> transactionList) {
        return transactionList
                .stream()
                .filter(x -> SELL.equals(x.getTransactionStatus().getStatus())).collect(Collectors.toList());
    }

    private List<Transaction> getAllBuyTransaction(List<Transaction> transactionList) {
        return transactionList
                .stream()
                .filter(x -> SELL.equals(x.getTransactionStatus().getStatus())).collect(Collectors.toList());
    }

    private List<Transaction> getAllTransactionByStatus(String status, List<Transaction> transactionList) {
        List<Transaction> transactions = transactionList
                .stream()
                .filter(x -> status.equals(x.getTransactionStatus().getStatus())).sorted((o1, o2) -> o1.getTransactionDate().isAfter(o2.getTransactionDate()) ? 1 : 0).collect(Collectors.toList());
        return transactions;
    }
}
