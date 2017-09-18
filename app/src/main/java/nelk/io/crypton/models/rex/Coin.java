package nelk.io.crypton.models.rex;

import nelk.io.crypton.models.enums.Crypto;
import nelk.io.crypton.models.enums.Fiat;
import nelk.io.crypton.models.utils.Value;

public class Coin {

    String name;
    String longName;
    String logoUrl;

    public Coin(String name, String longName, String logoUrl){
        this.name = name;
        this.longName = longName;
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

}
