package org.example.model.dao.constant;

public class TableColumns {

    public static class DoctorTable {
        public static final String ID = "DOCTOR_ID";
        public static final String NAME = "NAME";
        public static final String SURNAME = "SURNAME";
        public static final String PATRONYMIC = "PATRONYMIC";
        public static final String SPECIALIZATION = "SPECIALIZATION";
    }

    public static class PatientTable {
        public static final String ID = "PATIENT_ID";
        public static final String NAME = "NAME";
        public static final String SURNAME = "SURNAME";
        public static final String PATRONYMIC = "PATRONYMIC";
        public static final String PHONE_NUMBER = "PHONE_NUMBER";
    }

    public static class RecipeTable {
        public static final String ID = "RECIPE_ID";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String PATIENT_ID = "PATIENT_ID";
        public static final String DOCTOR_ID = "DOCTOR_ID";
        public static final String CREATION_DATE = "CREATION_DATE";
        public static final String PERIOD_OF_VALIDITY = "PERIOD_OF_VALIDITY";
        public static final String PRIORITY = "PRIORITY";
    }

}
