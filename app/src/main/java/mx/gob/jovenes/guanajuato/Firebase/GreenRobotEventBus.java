package mx.gob.jovenes.guanajuato.Firebase;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by codigus on 26/06/2017.
 */

public class GreenRobotEventBus {
    EventBus eventBus;

    private static class SingletonHolder {
        private static final GreenRobotEventBus INSTANCE = new GreenRobotEventBus();
    }

    public static GreenRobotEventBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GreenRobotEventBus() {
        this.eventBus = EventBus.getDefault();
    }

    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void unRegister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    public void post(Object event) {
        eventBus.post(event);
    }
}
