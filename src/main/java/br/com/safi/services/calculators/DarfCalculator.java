package br.com.safi.services.calculators;

import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.services.interfaces.ICalcTax;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DarfCalculator implements ICalcTax {

    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

/*
* "hasDebit": true, //balance > 0 && volumeTradedSell > 35000
	"volumeTradedBuy": 40000,
	"volumeTradedSell": 20000,
	"debitValue": 15.000,00,
	"expirationDate": "20/06/2022",
	"balance": -1.000,00,
	"month": 'Maio'	*/

    @Override
    public void calcTax(List<Transaction> transactionList) {

        //TODO: Pegar todas as transações de uma determinada moeda. DONE
        //TODO: Ordenar por data ascendente. DONE
        //TODO: Criar variável de quantidade atual e valor total atual.
        //TODO: Em ordem de compra somar a quantidade atual e o valor total atual.
        //TODO: Criar variável 'balancer'
        //TODO: Em ordem de venda atualizar variável balancer.
        //TODO: Atualizar a variável quantidade atual = (Quantidade atual - Quantidade vendida) e
        //valor total atual = (valor total atual - (Quantidade Vendida * Valor total atual))

        for (int month = 1; month < 12; month++) {
            int finalMonth = month;

            Map<Currency, List<Transaction>> map = new HashMap<>();

            List<Transaction> monthTransaction = transactionList
                    .stream()
                    .filter(x -> x.getTransactionDate()
                            .getMonthValue() == finalMonth).collect(Collectors.toList());

            var mapTransactionList = monthTransaction
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCurrency)).entrySet();

            mapTransactionList.forEach((mapTransactions) -> {
                Currency currentCurreny = mapTransactions.getKey();
                var qtdAtual = 0;
                var totalValue = 0;
                var transactions = mapTransactions.getValue();
                var totalUnitSell = getTotalAmount(SELL, transactions);
                var totalUnitWallet = getTotalQuantityInWallet(transactions);

                getAllTransactionByStatus(BUY, transactions).forEach((buyTransaction) -> {
                    getAllTransactionByStatus(SELL, transactions).forEach((transaction) -> {
                        var totalSell = transaction.getCurrencyValue();
                        var quantitySell = transaction.getCurrencyQuantity();
                        var quantityBuy = buyTransaction.getCurrencyQuantity();
                        var totalBuy = buyTransaction.getCurrencyValue();
                    });
                });


                //Transação referente a uma determinada moeda.
            /*    mapTransactions.getValue().forEach((transaction) -> {
                    transaction.
                    System.out.println(transaction);
               });*/
            });

            List<Transaction> sellTransaction = getTotalSellTransaction(transactionList);
            //calculateTax(totalBuyAmount, sellTransaction);
        }
    }

    private void calculateTax(BigDecimal totalBuyAmount, List<Transaction> sellTransaction) {
        sellTransaction.forEach((x) -> {
            //Valor da venda.
            BigDecimal saleValue = x.getCurrencyValue().multiply(x.getCurrencyQuantity());
        });
    }

    private boolean needToPayTax(BigDecimal totalSellAmount) {
        return totalSellAmount.compareTo(new BigDecimal(35000)) > 0;
    }

    private BigDecimal getTotalAmount(String buyOrSell, List<Transaction> transactionList) {
        var result = transactionList
                .stream()
                .filter(x -> buyOrSell.equals(x.getTransactionStatus().getStatus()))
                .map(Transaction::getCurrencyValue)
                .reduce(BigDecimal::add);
        return result.orElse(BigDecimal.ZERO);
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


    private BigDecimal getTotalQuantityInWallet(List<Transaction> transactionList) {
        return transactionList.stream().map(Transaction::getCurrencyQuantity).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
