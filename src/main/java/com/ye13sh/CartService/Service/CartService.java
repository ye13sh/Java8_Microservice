package com.ye13sh.CartService.Service;

import com.ye13sh.CartService.DTO.CommonDTO;
import com.ye13sh.CartService.Model.Cart;
import com.ye13sh.CartService.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    CartRepository repository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    RestTemplate restTemplate;

    private static final String url= "http://localhost:8080/getProduct/";
    private static final String cartOrderURL= "http://localhost:7070/cart/order";

    private static final String cartSunQuantityOrderURL= "http://localhost:8080/update/cart/product";
    public Cart insertCart(CommonDTO dto,String productName) {
        Cart cart = new Cart();

        // delete duplicate products in cart
        List<Cart> cartList = repository.findAll();
        List<Cart> filteredCart = cartList.stream().filter(x -> x.getProductName().equals(productName))
                .collect(Collectors.toList());

        if (filteredCart.size() > 0){
            throw new RuntimeException("Product already added to the cart");
            //return null;
        }


        if (dto.getProductName().equals(productName)) {
            cart.setProductName(dto.getProductName());
            cart.setQuantity(dto.getQuantity());
            cart.setPrice(dto.getPrice());
            cart.setProductID(dto.getProductID());

            return repository.save(cart);
        }else {
            throw new RuntimeException("Problem in insert");
        }
    }
    public List<Cart> getAllCartProducts(){
        return repository.findAll();
    }

    public Cart updateCart(CommonDTO dto,String productName){
        Cart cart = repository.findByProductName(productName);

        //if product service used ResponseEntity<Product> in its controller then you should use CommonDTO.class instead of String.class
        ResponseEntity<CommonDTO> response= restTemplate.getForEntity(url+cart.getProductName(),CommonDTO.class);
        CommonDTO commonDTO=response.getBody();

        if(cart!=null){
            cart.setQuantity(dto.getQuantity());
            cart.setPrice(dto.getQuantity() * commonDTO.getPrice()); //if you update quantity from cart, price has to change according to quantity
            // and price will come from product-service
        }
        return repository.save(cart);
    }
    public Cart updateCartProduct(CommonDTO dto,String productName){ // when product-service updates its price in cart-service it needs to update with according to quantity
        Cart cart = repository.findByProductName(productName);

        ResponseEntity<CommonDTO> response= restTemplate.getForEntity(url+cart.getProductName(),CommonDTO.class);
        CommonDTO commonDTO=response.getBody();

        if(cart!=null){
            cart.setPrice(cart.getQuantity() * commonDTO.getPrice());// when product-service updates its price in cart-service it needs to update with according to quantity
        }
        return repository.save(cart);
    }

    public void deleteCartItem(String productName){
        repository.deleteByProductName(productName);
    }


//    public void subProductQuantity(List<String> productName){
//        for (String prodNames : productName){
//            Cart cart1 = repository.findByProductName(prodNames);
//            if (cart1 == null) {
//                throw new RuntimeException("Error in fetching product");
//            }
//
//            ResponseEntity<?> dtoResponse = restTemplate.getForEntity(url + prodNames, null, CommonDTO.class);
//            CommonDTO commonDTO = (CommonDTO) dtoResponse.getBody();
//
//            if (commonDTO != null && commonDTO.getProductName() != null){
//                List<Cart> cart = repository.getByProductName(prodNames);
//                for (Cart cartList : cart){
//                    if(commonDTO.getQuantity() >= 0) {
//                        commonDTO.setQuantity(commonDTO.getQuantity() - cartList.getQuantity());
//                    } else {
//                        throw new RuntimeException("Product quantity is showing 0");
//                    }
//                }
//                ResponseEntity<?> response = makeRequest(cartSunQuantityOrderURL, HttpMethod.POST, commonDTO, String.class);
//                String s = (String) response.getBody();
//            } else {
//                throw new RuntimeException("Error in fetching product");
//            }
//        }
//    }
//
//    public ResponseEntity<?> makeRequest(String url, HttpMethod method, Object requestObject, Class<?> responseType) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);
//
//        return restTemplate.exchange(url, method, requestEntity, responseType);
//    }



    public String generateOrderID(){
        Random random = new Random();
        int randomID = 10000+random.nextInt(99999);
        return String.format("%04d",randomID);
    }

    public void cartBuyOrder(){
        List<Cart> cartList= repository.findAll();
        Double totalPrice = repository.sumOfPrice();

        String orderID=generateOrderID();

        for (Cart cart:cartList) {
            if (!cartList.isEmpty()) {
                LocalDateTime DateOfPurchase = LocalDateTime.now();
                CommonDTO dto = new CommonDTO();

                dto.setProductName(cart.getProductName());
                dto.setQuantity(cart.getQuantity());
                dto.setTotalPrice(totalPrice);
                dto.setProductID(cart.getProductID());
                //dto.setOrderID(orderID); //if you want same orderId for your entire purchase from cart

                ResponseEntity<String> response = restTemplate.postForEntity(cartOrderURL, dto, String.class);
                String string = response.getBody();
            }
        }
    }
    public void deleteCart(){
        repository.deleteAll();
    }

}
