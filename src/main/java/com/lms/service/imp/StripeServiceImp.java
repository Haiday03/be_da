package com.lms.service.imp;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Category;
import com.lms.model.LibraryUser;
import com.lms.model.stripe.CheckoutPayment;
import com.lms.model.stripe.DashboardInfo;
import com.lms.model.stripe.Subscription;
import com.lms.repository.*;
import com.lms.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StripeServiceImp implements StripeService {

    private final StripeRepository stripeRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public CheckoutPayment save(CheckoutPayment checkoutPayment) {
        return stripeRepository.save(checkoutPayment);
    }



    @Override
    public List<CheckoutPayment> findAll(){
        return stripeRepository.findAll();
    }

    @Override
    public DashboardInfo getDashboardInfo() {

        DashboardInfo dashboardInfo = new DashboardInfo();
        List<LibraryUser> userList = this.userRepository.findAll();
        List<Author> authorList = this.authorRepository.findAll();
        List<Category> categoryList = this.categoryRepository.findAll();
        List<Book> bookList = this.bookRepository.findAll();

        //User Size
        dashboardInfo.setCountUsers(userList.size());
        //Author Size
        dashboardInfo.setCountAuthors(authorList.size());
        //Category Size
        dashboardInfo.setCountCategories(categoryList.size());
        //Book Size
        dashboardInfo.setCountBooks(bookList.size());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date startDate = cal.getTime();
        Date endDate = new Date();

        List<LibraryUser> userList1 = this.userRepository.findByCreatedDateBetween(startDate, endDate);
        List<Author> authorList1 = this.authorRepository.findByCreatedDateBetween(startDate, endDate);
        List<Category> categoryList1 = this.categoryRepository.findByCreatedDateBetween(startDate, endDate);
        List<Book> bookList1 = this.bookRepository.findByCreatedDateBetween(startDate, endDate);

        //New user Size
        dashboardInfo.setCountNewUsers(userList1.size());
        //New author Size
        dashboardInfo.setCountNewAuthors(authorList1.size());
        //New category Size
        dashboardInfo.setCountNewCategories(categoryList1.size());
        //New book Size
        dashboardInfo.setCountNewBooks(bookList1.size());

//        //Revenue
//        double revenue=data.stream().mapToDouble(o->o.getAmount()).sum()/100;
//        dashboardInfo.setRevenue(revenue);
//
//        //get 7 days from now
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH, -7);
//
//        double percentageRevenue = data.stream().filter(f->f.getDateCreated().before(cal.getTime())).mapToDouble(r->r.getAmount()).sum()/100;
//        dashboardInfo.setPercentageRevenue(percentageRevenue);
//
//        double percentageOrders= data.stream().filter(f->f.getDateCreated().before(cal.getTime())).collect(Collectors.toList()).size();
//        dashboardInfo.setPercentageOrders(percentageOrders);
//
//        dashboardInfo.setSub(sub.size());
//        double percentageSubs=sub.stream().filter(f->f.getDateCreated().before(cal.getTime())).collect(Collectors.toList()).size();
//        dashboardInfo.setPercentageSub(percentageSubs);

        return dashboardInfo;
    }


}
