package com.iflytek.klma.iweather.gson;

/**
 * 热门城市列表获取，通过开放云平台配置关键词“热门城市”获得
 * 可实现热门城市列表更新功能
 * 热门城市列表为：
 * answer.text    string("北京、天津、上海、重庆、沈阳、大连、长春、哈尔滨、郑州、武汉")
 */

public class HotCity {

    /**
     * answer : {"answerType":"openQA","emotion":"default","question":{"question":"热门城市","question_ws":"热门/AA//  城市/NN//"},"text":"北京、天津、上海、重庆、沈阳、大连、长春、哈尔滨、郑州、武汉、长沙、广州、合肥、深圳、南京、西安、无锡、常州、苏州、杭州、宁波、济南、青岛、福州、厦门、成都、昆明","topic":33606425643688550,"topicID":33606425643688550,"type":"T"}
     * man_intv :
     * no_nlu_result : 0
     * operation : ANSWER
     * rc : 0
     * service : openQA
     * status : 0
     * text : 热门城市
     * uuid : atn001690f5@un6f730cd7832d6f2a01
     * sid : atn001690f5@un6f730cd7832d6f2a01
     */

    private AnswerBean answer;
    private String man_intv;
    private int no_nlu_result;
    private String operation;
    private int rc;
    private String service;
    private int status;
    private String text;
    private String uuid;
    private String sid;

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getMan_intv() {
        return man_intv;
    }

    public void setMan_intv(String man_intv) {
        this.man_intv = man_intv;
    }

    public int getNo_nlu_result() {
        return no_nlu_result;
    }

    public void setNo_nlu_result(int no_nlu_result) {
        this.no_nlu_result = no_nlu_result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static class AnswerBean {
        /**
         * answerType : openQA
         * emotion : default
         * question : {"question":"热门城市","question_ws":"热门/AA//  城市/NN//"}
         * text : 北京、天津、上海、重庆、沈阳、大连、长春、哈尔滨、郑州、武汉、长沙、广州、合肥、深圳、南京、西安、无锡、常州、苏州、杭州、宁波、济南、青岛、福州、厦门、成都、昆明
         * topic : 33606425643688550
         * topicID : 33606425643688550
         * type : T
         */

        private String answerType;
        private String emotion;
        private QuestionBean question;
        private String text;
        private long topic;
        private long topicID;
        private String type;

        public String getAnswerType() {
            return answerType;
        }

        public void setAnswerType(String answerType) {
            this.answerType = answerType;
        }

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }

        public QuestionBean getQuestion() {
            return question;
        }

        public void setQuestion(QuestionBean question) {
            this.question = question;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getTopic() {
            return topic;
        }

        public void setTopic(long topic) {
            this.topic = topic;
        }

        public long getTopicID() {
            return topicID;
        }

        public void setTopicID(long topicID) {
            this.topicID = topicID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class QuestionBean {
            /**
             * question : 热门城市
             * question_ws : 热门/AA//  城市/NN//
             */

            private String question;
            private String question_ws;

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getQuestion_ws() {
                return question_ws;
            }

            public void setQuestion_ws(String question_ws) {
                this.question_ws = question_ws;
            }
        }
    }
}
