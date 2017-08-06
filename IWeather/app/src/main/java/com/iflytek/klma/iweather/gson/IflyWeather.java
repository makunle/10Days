package com.iflytek.klma.iweather.gson;

import java.util.List;

/**
 * 语音云返回的天气json数据解析类
 * 可使用的值：
 * 1、text:原始的语音内容
 * 2、data.result(0).city:城市名
 */

public class IflyWeather implements Weather {

    @Override
    public String getInfo() {
        return answer.text;
    }

    @Override
    public String getNowTemperature() {
        return null;
    }

    @Override
    public String getMinTemperature() {
        return null;
    }

    @Override
    public String getMaxTemperature() {
        return null;
    }

    @Override
    public String getWindDirect() {
        return null;
    }

    @Override
    public String getWindLevel() {
        return null;
    }

    @Override
    public String getUpdateTime() {
        return null;
    }

    /**
     * data : {"result":[{"airData":53,"airQuality":"良","city":"合肥","date":"2017-07-29","dateLong":1501257600,"exp":{"ct":{"expName":"穿衣指数","level":"炎热","prompt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}},"humidity":"56%","lastUpdateTime":"2017-07-29 12:39:15","pm25":"36","temp":35,"tempRange":"28℃~37℃","weather":"多云","weatherType":1,"wind":"东北风3-4级","windLevel":1},{"city":"合肥","date":"2017-07-30","dateLong":1501344000,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"28℃~37℃","weather":"雷阵雨","weatherType":4,"wind":"东风3-4级","windLevel":1},{"city":"合肥","date":"2017-07-31","dateLong":1501430400,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"25℃~34℃","weather":"中雨","weatherType":8,"wind":"北风微风","windLevel":0},{"city":"合肥","date":"2017-08-01","dateLong":1501516800,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"27℃~36℃","weather":"小雨","weatherType":7,"wind":"北风4-5级","windLevel":2},{"city":"合肥","date":"2017-08-02","dateLong":1501603200,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"27℃~36℃","weather":"小雨转阴","weatherType":7,"wind":"西北风4-5级","windLevel":2},{"city":"合肥","date":"2017-08-03","dateLong":1501689600,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"27℃~36℃","weather":"阴","weatherType":2,"wind":"西北风4-5级","windLevel":2},{"city":"合肥","date":"2017-08-04","dateLong":1501776000,"lastUpdateTime":"2017-07-29 12:39:15","tempRange":"28℃~37℃","weather":"阴","weatherType":2,"wind":"西北风4-5级","windLevel":2}]}
     * rc : 0
     * semantic : [{"intent":"QUERY","slots":[{"name":"location.city","value":"合肥市","normValue":"合肥市"},{"name":"location.cityAddr","value":"合肥","normValue":"合肥"},{"name":"location.type","value":"LOC_BASIC","normValue":"LOC_BASIC"},{"name":"queryType","value":"内容"},{"name":"subfocus","value":"天气状态"}]}]
     * service : weather
     * text : 合肥天气
     * uuid : atn002f6d5d@un46360cd780456f2601
     * used_state : {"state_key":"fg::weather::default::default","state":"default"}
     * answer : {"text":"\"合肥\"今天\"多云\"，\"28℃~37℃\"，\"东北风3-4级\""}
     * dialog_stat : DataValid
     * sid : sch337fe700@ch47750cd78045477500
     */

    public String getCountyName() {
        return data.result.get(0).city;
    }

    @Override
    public boolean isDataUsable() {
        return "DataValid".equals(dialog_stat);
    }

    @Override
    public String getAirQuality() {
        return null;
    }

    @Override
    public String getPM25() {
        return null;
    }

    @Override
    public String getComfortInfo() {
        return null;
    }

    @Override
    public String getCarWashInfo() {
        return null;
    }

    @Override
    public String getSportInfo() {
        return null;
    }

    @Override
    public String getAQI() {
        return null;
    }

    @Override
    public String getExtraInfo() {
        return text;
    }

    @Override
    public List<Forecast> getTwoDaysForecast() {
        return null;
    }

    private DataBean data;
    private int rc;
    private String service;
    private String text;
    private String uuid;
    private UsedStateBean used_state;
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

    public UsedStateBean getUsed_state() {
        return used_state;
    }

    public void setUsed_state(UsedStateBean used_state) {
        this.used_state = used_state;
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
             * airData : 53
             * airQuality : 良
             * city : 合肥
             * date : 2017-07-29
             * dateLong : 1501257600
             * exp : {"ct":{"expName":"穿衣指数","level":"炎热","prompt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}}
             * humidity : 56%
             * lastUpdateTime : 2017-07-29 12:39:15
             * pm25 : 36
             * temp : 35
             * tempRange : 28℃~37℃
             * weather : 多云
             * weatherType : 1
             * wind : 东北风3-4级
             * windLevel : 1
             */

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

    public static class UsedStateBean {
        /**
         * state_key : fg::weather::default::default
         * state : default
         */

        private String state_key;
        private String state;

        public String getState_key() {
            return state_key;
        }

        public void setState_key(String state_key) {
            this.state_key = state_key;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class AnswerBean {
        /**
         * text : "合肥"今天"多云"，"28℃~37℃"，"东北风3-4级"
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
         * slots : [{"name":"location.city","value":"合肥市","normValue":"合肥市"},{"name":"location.cityAddr","value":"合肥","normValue":"合肥"},{"name":"location.type","value":"LOC_BASIC","normValue":"LOC_BASIC"},{"name":"queryType","value":"内容"},{"name":"subfocus","value":"天气状态"}]
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
             * name : location.city
             * value : 合肥市
             * normValue : 合肥市
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
