package org.aery.study.spring.ws.way1;

import org.springframework.stereotype.Component;

@Component
public class UserVerifier {

    public void verify(String token) {
        // TODO 將token拿去問Authorization Server, 看合不合法啊, 拿到一些user資訊之類的
    }

}
