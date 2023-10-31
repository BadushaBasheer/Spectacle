package com.ecommerce.library.service;

import com.ecommerce.library.dto.WalletHistoryDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.model.WalletHistory;

import java.util.List;

public interface WalletService {

    List<WalletHistoryDto> findAllById(long id);

    Wallet findByCustomer(Customer customer);

    WalletHistory save(double amount, Customer customer);
    boolean saveReferralOffer(double amount, Customer customer);

    WalletHistory findById(long id);

    void updateWallet(WalletHistory walletHistory,Customer customer,boolean status);

    WalletHistory debit(Wallet wallet,double totalPrice);

    void returnCredit(Order order, Customer customer);

    void saveOrderId(Order order, WalletHistory walletHistory);


}
