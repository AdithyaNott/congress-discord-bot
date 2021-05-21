package congressbot.legislation;

import congressbot.politicians.CongressSponsor;
import congressbot.politicians.CosponsorInfo;

import java.util.Date;

public abstract class CongressBill {
    private final String billUrl;
    private final String shortTitle;
    private final String longTitle;

    private final String summary;

    private final CongressSponsor sponsor;

    private final CosponsorInfo cosponsorInfo;

    private final LegislativeStep[] legislativeSteps;

    private final String explanation;

    protected CongressBill(CongressBillBuilder cb, String explanation, LegislativeStep... legislativeSteps) {
        this.billUrl = cb.getBillUrl();
        this.shortTitle = cb.getShortTitle();
        this.longTitle = cb.getLongTitle();

        this.summary = cb.getSummary();

        this.sponsor = cb.getSponsor();

        this.cosponsorInfo = cb.getCosponsorInfo();

        this.legislativeSteps = legislativeSteps;
        this.explanation = explanation;
    }

    protected void setLegislativeStepStatus(int index, boolean isPassed, Date date) {
        legislativeSteps[index].setStatus(isPassed, date);
    }

    protected void setLegislativeStep(int index, LegislativeStep step) {
        legislativeSteps[index] = step;
    }

    public LegislativeStep getLegislativeStep(int index) {
        return legislativeSteps[index];
    }

    public int getLegislativeStepsLength() {
        return legislativeSteps.length;
    }

    public String getBillUrl() {
        return billUrl;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getSummary() {
        return summary;
    }

    public CongressSponsor getSponsor() {
        return sponsor;
    }

    public CosponsorInfo getCosponsorInfo() {
        return cosponsorInfo;
    }

    public static class CongressBillBuilder {
        private String billType;
        private String billUrl;
        private String shortTitle = "";
        private String longTitle = "";
        private CongressSponsor sponsor;
        private CosponsorInfo cosponsorInfo;
        private String summary = "";

        public static CongressBillBuilder newInstance(String billType) {
            CongressBillBuilder cb = new CongressBillBuilder();
            cb.billType = billType;
            return cb;
        }

        public String getBillUrl() {
            return billUrl;
        }

        public String getShortTitle() {
            return shortTitle;
        }

        public String getLongTitle() {
            return longTitle;
        }

        public CongressSponsor getSponsor() {
            return sponsor;
        }

        public CosponsorInfo getCosponsorInfo() {
            return cosponsorInfo;
        }


        public String getSummary() {
            return summary;
        }

        public CongressBill build() {
            CongressBill bill = null;
            switch (billType) {
                case "hr":
                    bill = new HouseBill(this);
                    break;
                case "s":
                    bill = new SenateBill(this);
                    break;
                case "hres":
                    bill = new HouseResolution(this);
                    break;
                case "sres":
                    bill = new SenateResolution(this);
                    break;
                case "hconres":
                    bill = new HouseConcurrentResolution(this);
                    break;
                case "sconres":
                    bill = new SenateConcurrentResolution(this);
                    break;
                case "hjres":
                    bill = new HouseJointResolution(this);
                    break;
                case "sjres":
                    bill = new SenateJointResolution(this);
                    break;
            }
            return bill;
        }

        public CongressBillBuilder billUrl(String billUrl) {
            this.billUrl = billUrl;
            return this;
        }

        public CongressBillBuilder shortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
            return this;
        }

        public CongressBillBuilder longTitle(String longTitle) {
            this.longTitle = longTitle;
            return this;
        }

        public CongressBillBuilder sponsors(CongressSponsor sponsor, CosponsorInfo cosponsorInfo) {
            this.sponsor = sponsor;
            this.cosponsorInfo = cosponsorInfo;

            return this;
        }

        public CongressBillBuilder summary(String summary) {
            StringBuilder sb = new StringBuilder(summary);
            if (sb.length() > 1024) {
                sb.setLength(1024);
                sb.replace(1021, 1024, "...");
            }
            this.summary = sb.toString();
            return this;
        }

    }
}