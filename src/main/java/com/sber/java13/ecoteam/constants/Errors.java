package com.sber.java13.ecoteam.constants;

public interface Errors {
    class Wastes {
        public static final String WASTE_DELETE_ERROR = "Отход не может быть удален, так как на него есть активные заказы";
    }
    
    class Orders {
        public static final String ORDER_DELETE_ERROR = "Заказ не может быть удалён, так как он выполнен";
    }
    
    class Users {
        public static final String USER_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о пользователе";
        public static final String USER_DELETE_ERROR = "Пользователь не может быть удалён, так как у него есть активные заявки";
    }
    
    class Points {
        public static final String POINT_FORBIDDEN_ERROR = "Пункт приёма не может быть удалён, так как у него есть активный вид мусора";
    }
}
