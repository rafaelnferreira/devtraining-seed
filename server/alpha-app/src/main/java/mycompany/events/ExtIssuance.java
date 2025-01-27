package mycompany.events;

import java.util.Objects;

public class ExtIssuance {

    private String tradeId;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExtIssuance that = (ExtIssuance) o;
        return Objects.equals(tradeId, that.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tradeId);
    }
}
