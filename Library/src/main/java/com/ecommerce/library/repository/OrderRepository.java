package com.ecommerce.library.repository;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Order findById(long id);

    List<Order> findByCustomer(Customer customer);

    List<Order> findByOrderDateBetween(Date startDate, Date endDate);

    int countByIsAcceptIsFalse();

    long count();

    @Query(value = "SELECT SUM(o.totalPrice) FROM Order o WHERE MONTH(o.orderDate)=:month AND YEAR(o.orderDate)=:year",nativeQuery = true)
    int totalPrice(YearMonth year, Month month);

    @Query(value="SELECT DATE_TRUNC('day', o.order_date) AS date, SUM(SUM(o.total_price)) OVER (PARTITION BY DATE_TRUNC('day', o.order_date)) AS earnings FROM orders o WHERE EXTRACT(YEAR FROM o.order_date) = :year AND EXTRACT(MONTH FROM o.order_date) =:month GROUP BY DATE_TRUNC('day', o.order_date)",nativeQuery = true)
    List<Object[]> dailyEarnings(@Param("year") int year, @Param("month") int month);

    @Query("SELECT o.paymentMethod, SUM(o.totalPrice) FROM Order o WHERE o.paymentMethod IN ('COD', 'RazorPay', 'Wallet') GROUP BY o.paymentMethod")
    List<Object[]> findTotalPricesByPaymentMethod();
}
