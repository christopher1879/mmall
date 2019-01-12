package vo;

import java.math.BigDecimal;
import java.util.List;

public class cartvo
{
  private List<cartproductvo> cartproductvoList;
  private BigDecimal carttotalprice;
  private Boolean allchecked;
  private String imagehost;

    public List<cartproductvo> getCartproductvoList() {
        return cartproductvoList;
    }

    public void setCartproductvoList(List<cartproductvo> cartproductvoList) {
        this.cartproductvoList = cartproductvoList;
    }

    public BigDecimal getCarttotalprice() {
        return carttotalprice;
    }

    public void setCarttotalprice(BigDecimal carttotalprice) {
        this.carttotalprice = carttotalprice;
    }

    public Boolean getAllchecked() {
        return allchecked;
    }

    public void setAllchecked(Boolean allchecked) {
        this.allchecked = allchecked;
    }

    public String getImagehost() {
        return imagehost;
    }

    public void setImagehost(String imagehost) {
        this.imagehost = imagehost;
    }
}
