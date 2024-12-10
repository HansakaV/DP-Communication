package lk.ijse.dpcommunication.model;

public class bill {
    String billId;

    public bill(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Override
    public String toString() {
        return "bill{" +
                "billId='" + billId + '\'' +
                '}';
    }
}
