package com.yzy.ebag.student.bean;

import java.util.List;

/**
 * Created by fansan on 2018/4/9.
 */

public class LeaningProgressBean {


    /**
     * schoolName :
     * learningProcessClassDtos : [{"className":"123","headmaster":"jbq\t","teacherDtos":[{"teacherName":"jbq\t","curriculum":"语文"}]}]
     */

    private String schoolName;
    private List<LearningProcessClassDtosBean> learningProcessClassDtos;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<LearningProcessClassDtosBean> getLearningProcessClassDtos() {
        return learningProcessClassDtos;
    }

    public void setLearningProcessClassDtos(List<LearningProcessClassDtosBean> learningProcessClassDtos) {
        this.learningProcessClassDtos = learningProcessClassDtos;
    }

    public static class LearningProcessClassDtosBean {
        /**
         * className : 123
         * headmaster : jbq
         * teacherDtos : [{"teacherName":"jbq\t","curriculum":"语文"}]
         */

        private String className;
        private String headmaster;
        private List<TeacherDtosBean> teacherDtos;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getHeadmaster() {
            return headmaster;
        }

        public void setHeadmaster(String headmaster) {
            this.headmaster = headmaster;
        }

        public List<TeacherDtosBean> getTeacherDtos() {
            return teacherDtos;
        }

        public void setTeacherDtos(List<TeacherDtosBean> teacherDtos) {
            this.teacherDtos = teacherDtos;
        }

        public static class TeacherDtosBean {
            /**
             * teacherName : jbq
             * curriculum : 语文
             */

            private String teacherName;
            private String curriculum;

            public String getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(String teacherName) {
                this.teacherName = teacherName;
            }

            public String getCurriculum() {
                return curriculum;
            }

            public void setCurriculum(String curriculum) {
                this.curriculum = curriculum;
            }
        }
    }
}
