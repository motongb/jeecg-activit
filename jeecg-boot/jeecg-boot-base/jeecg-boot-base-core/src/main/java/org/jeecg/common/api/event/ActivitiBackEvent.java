package org.jeecg.common.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author motb
 * @date 2021/2/20 14:28
 * @description //TODO ActivitiBackEvent
 **/
public class ActivitiBackEvent<T> extends ApplicationEvent {

    private T data;

    public ActivitiBackEvent(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
