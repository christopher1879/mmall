package vo;

import java.math.BigDecimal;

public class cartproductvo {
    private Integer id;
    private Integer userid;
    private Integer productid;
    private String productname;
    private  String productimage;
    private String productsubtitle;
    private  String productmainimage;
    private BigDecimal productprice;
    private BigDecimal producttotalprice;
    private  Integer productchecked;
    private  Integer productstatus;
    private Integer productstock;
    private String limitquantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductsubtitle() {
        return productsubtitle;
    }

    public void setProductsubtitle(String productsubtitle) {
        this.productsubtitle = productsubtitle;
    }

    public String getProductmainimage() {
        return productmainimage;
    }

    public void setProductmainimage(String productmainimage) {
        this.productmainimage = productmainimage;
    }

    public BigDecimal getProductprice() {
        return productprice;
    }

    public void setProductprice(BigDecimal productprice) {
        this.productprice = productprice;
    }

    public BigDecimal getProducttotalprice() {
        return producttotalprice;
    }

    public void setProducttotalprice(BigDecimal producttotalprice) {
        this.producttotalprice = producttotalprice;
    }

    public Integer getProductchecked() {
        return productchecked;
    }

    public void setProductchecked(Integer productchecked) {
        this.productchecked = productchecked;
    }

    public Integer getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(Integer productstatus) {
        this.productstatus = productstatus;
    }

    public Integer getProductstock() {
        return productstock;
    }

    public void setProductstock(Integer productstock) {
        this.productstock = productstock;
    }

    public String getLimitquantity() {
        return limitquantity;
    }

    public void setLimitquantity(String limitquantity) {
        this.limitquantity = limitquantity;
    }
}
