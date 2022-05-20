package br.com.safi.services.calculators;

import br.com.safi.models.Currency;
import br.com.safi.models.Transaction;
import br.com.safi.services.interfaces.ICalcTax;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DarfCalculator implements ICalcTax {

    private static final String BUY = "BUY";
    private static final String SELL = "SELL";


    @Override
    public void calcTax(List<Transaction> transactionList) {

        //TODO: Pegar todas as transações de uma determinada moeda.
        //TODO: Ordenar por data ascendente.
        //TODO: Criar variável de quantidade atual e valor total atual.
        //TODO: Em ordem de compra somar a quantidade atual e o valor total atual.
        //TODO: Criar variável 'balancer'
        //TODO: Em ordem de venda atualizar variável balancer.
        //TODO: Atualizar a variável quantidade atual = (Quantidade atual - Quantidade vendida) e
               //valor total atual = (valor total atual - (Quantidade Vendida * Valor total atual))

        for (int month = 1; month <= transactionList.size(); month++) {
            int finalMonth = month;

            Map<Currency, List<Transaction>> map = new HashMap<>();

            List<Transaction> monthTransaction = transactionList
                    .stream()
                    .filter(x -> x.getTransactionDate()
                            .getMonthValue() == finalMonth).collect(Collectors.toList());

             var mapTransactionList = monthTransaction
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCurrency));


            BigDecimal totalBuyAmount = getTotalAmount(BUY, monthTransaction);
            BigDecimal totalSellAmount = getTotalAmount(SELL, monthTransaction);

            boolean needPayTax = needToPayTax(totalSellAmount);
            if(needPayTax) {
                List<Transaction> sellTransaction = getTotalSellTransaction(transactionList);
                calculateTax(totalBuyAmount, sellTransaction);
            }
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
        return transactionList
                .stream()
                .filter(x -> buyOrSell.equals(x.getTransactionStatus().getStatus()))
                .map(x -> x.getCurrencyValue().multiply(x.getCurrencyQuantity()))
                .reduce(BigDecimal::add)
                .get();
    }

    private List<Transaction> getTotalSellTransaction(List<Transaction> transactionList) {
        return transactionList
                .stream()
                .filter(x -> SELL.equals(x.getTransactionStatus().getStatus())).collect(Collectors.toList());
    }
}
