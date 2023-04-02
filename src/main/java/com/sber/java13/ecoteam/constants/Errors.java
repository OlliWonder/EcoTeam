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
    }
}
