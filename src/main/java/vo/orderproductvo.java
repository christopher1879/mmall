package vo;

import java.math.BigDecimal;
import java.util.*;

public class orderproductvo {
    private List<orderitemvo> orderitemvoList;
    private BigDecimal producttotalprice;
    private String imagehost;

    public List<orderitemvo> getOrderitemvoList() {
        return orderitemvoList;
    }

    public void setOrderitemvoList(List<orderitemvo> orderitemvoList) {
        this.orderitemvoList = orderitemvoList;
    }

    public BigDecimal getProducttotalprice() {
        return producttotalprice;
    }

    public void setProducttotalprice(BigDecimal producttotalprice) {
        this.producttotalprice = producttotalprice;
    }

    public String getImagehost() {
        return imagehost;
    }

    public void setImagehost(String imagehost) {
        this.imagehost = imagehost;
    }
}
