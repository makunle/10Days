package com.iflytek.mkl.vociesdktest.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class IflyWeather {

    /**
     * data : {"result":[{"airData":128,"airQuality":"轻微污染","city":"合肥","date":"2017-07-28","dateLong":1501171200,"exp":{"ct":{"expName":"穿衣指数","level":"炎热","prompt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}},"humidity":"44%","lastUpdateTime":"2017-07-28 13:33:17","pm25":"56","temp":39,"tempRange":"29℃~39℃","weather":"多云","weatherType":1,"wind":"东北风3-4级","windLevel":1},{"city":"合肥","date":"2017-07-29","dateLong":1501257600,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"29℃~38℃","weather":"多云","weatherType":1,"wind":"东北风3-4级","windLevel":1},{"city":"合肥","date":"2017-07-30","dateLong":1501344000,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"28℃~37℃","weather":"雷阵雨转阴","weatherType":4,"wind":"东风3-4级","windLevel":1},{"city":"合肥","date":"2017-07-31","dateLong":1501430400,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"26℃~36℃","weather":"小雨转多云","weatherType":7,"wind":"东风3-4级","windLevel":1},{"city":"合肥","date":"2017-08-01","dateLong":1501516800,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"26℃~33℃","weather":"小雨转阴","weatherType":7,"wind":"东风3-4级","windLevel":1},{"city":"合肥","date":"2017-08-02","dateLong":1501603200,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"26℃~32℃","weather":"小雨转多云","weatherType":7,"wind":"北风3-4级","windLevel":1},{"city":"合肥","date":"2017-08-03","dateLong":1501689600,"lastUpdateTime":"2017-07-28 13:33:17","tempRange":"26℃~33℃","weather":"小雨转阴","weatherType":7,"wind":"西北风3-4级","windLevel":1}]}
     * rc : 0
     * semantic : [{"intent":"QUERY","slots":[{"name":"datetime","value":"今天","normValue":"{\"datetime\":\"2017-07-28\",\"suggestDatetime\":\"2017-07-28\"}"},{"name":"location.city","value":"合肥市","normValue":"合肥市"},{"name":"location.cityAddr","value":"合肥","normValue":"合肥"},{"name":"location.type","value":"LOC_BASIC","normValue":"LOC_BASIC"},{"name":"queryType","value":"内容"},{"name":"subfocus","value":"天气状态"}]}]
     * service : weather
     * text : 合肥的天气。
     * uuid : atn0010a1ee@un6f730cd618a66f2a01
     * answer : {"text":"\"合肥今天多云\"，\"29℃~39℃\"，\"东北风3-4级\""}
     * dialog_stat : DataValid
     * sid : iat701a3cff@ch03090cd618a33de400
     */

    private DataBean data;
    private int rc;
    private String service;
    private String text;
    private String uuid;
    private AnswerBean answer;
    private String dialog_stat;
    private String sid;
    private List<SemanticBean> semantic;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getDialog_stat() {
        return dialog_stat;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<SemanticBean> getSemantic() {
        return semantic;
    }

    public void setSemantic(List<SemanticBean> semantic) {
        this.semantic = semantic;
    }

    public static class DataBean {
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * airData : 128
             * airQuality : 轻微污染
             * city : 合肥
             * date : 2017-07-28
             * dateLong : 1501171200
             * exp : {"ct":{"expName":"穿衣指数","level":"炎热","prompt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}}
             * humidity : 44%
             * lastUpdateTime : 2017-07-28 13:33:17
             * pm25 : 56
             * temp : 39
             * tempRange : 29℃~39℃
             * weather : 多云
             * weatherType : 1
             * wind : 东北风3-4级
             * windLevel : 1
             */

            public String info(){
                return "pm2.5指数"+airData+" "+airQuality+"\n\n"+exp.ct.prompt;
            }

            private int airData;
            private String airQuality;
            private String city;
            private String date;
            private int dateLong;
            private ExpBean exp;
            private String humidity;
            private String lastUpdateTime;
            private String pm25;
            private int temp;
            private String tempRange;
            private String weather;
            private int weatherType;
            private String wind;
            private int windLevel;

            public int getAirData() {
                return airData;
            }

            public void setAirData(int airData) {
                this.airData = airData;
            }

            public String getAirQuality() {
                return airQuality;
            }

            public void setAirQuality(String airQuality) {
                this.airQuality = airQuality;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getDateLong() {
                return dateLong;
            }

            public void setDateLong(int dateLong) {
                this.dateLong = dateLong;
            }

            public ExpBean getExp() {
                return exp;
            }

            public void setExp(ExpBean exp) {
                this.exp = exp;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public int getTemp() {
                return temp;
            }

            public void setTemp(int temp) {
                this.temp = temp;
            }

            public String getTempRange() {
                return tempRange;
            }

            public void setTempRange(String tempRange) {
                this.tempRange = tempRange;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public int getWeatherType() {
                return weatherType;
            }

            public void setWeatherType(int weatherType) {
                this.weatherType = weatherType;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public int getWindLevel() {
                return windLevel;
            }

            public void setWindLevel(int windLevel) {
                this.windLevel = windLevel;
            }

            public static class ExpBean {
                /**
                 * ct : {"expName":"穿衣指数","level":"炎热","prompt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}
                 */

                private CtBean ct;

                public CtBean getCt() {
                    return ct;
                }

                public void setCt(CtBean ct) {
                    this.ct = ct;
                }

                public static class CtBean {
                    /**
                     * expName : 穿衣指数
                     * level : 炎热
                     * prompt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
                     */

                    private String expName;
                    private String level;
                    private String prompt;

                    public String getExpName() {
                        return expName;
                    }

                    public void setExpName(String expName) {
                        this.expName = expName;
                    }

                    public String getLevel() {
                        return level;
                    }

                    public void setLevel(String level) {
                        this.level = level;
                    }

                    public String getPrompt() {
                        return prompt;
                    }

                    public void setPrompt(String prompt) {
                        this.prompt = prompt;
                    }
                }
            }
        }
    }

    public static class AnswerBean {
        /**
         * text : "合肥今天多云"，"29℃~39℃"，"东北风3-4级"
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class SemanticBean {
        /**
         * intent : QUERY
         * slots : [{"name":"datetime","value":"今天","normValue":"{\"datetime\":\"2017-07-28\",\"suggestDatetime\":\"2017-07-28\"}"},{"name":"location.city","value":"合肥市","normValue":"合肥市"},{"name":"location.cityAddr","value":"合肥","normValue":"合肥"},{"name":"location.type","value":"LOC_BASIC","normValue":"LOC_BASIC"},{"name":"queryType","value":"内容"},{"name":"subfocus","value":"天气状态"}]
         */

        private String intent;
        private List<SlotsBean> slots;

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public List<SlotsBean> getSlots() {
            return slots;
        }

        public void setSlots(List<SlotsBean> slots) {
            this.slots = slots;
        }

        public static class SlotsBean {
            /**
             * name : datetime
             * value : 今天
             * normValue : {"datetime":"2017-07-28","suggestDatetime":"2017-07-28"}
             */

            private String name;
            private String value;
            private String normValue;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getNormValue() {
                return normValue;
            }

            public void setNormValue(String normValue) {
                this.normValue = normValue;
            }
        }
    }
}
