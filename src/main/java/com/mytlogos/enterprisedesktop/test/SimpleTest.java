package com.mytlogos.enterprisedesktop.test;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleTest {
    public static void main(String[] args) throws Exception {
        AtomicInteger integer = new AtomicInteger();

        final Observable<Integer> flowable = Observable.create(emitter -> {
            Thread thread = new Thread(() -> {
                emitter.onNext(integer.getAndIncrement());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emitter.onNext(integer.getAndIncrement());
                emitter.onNext(integer.getAndIncrement());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emitter.onNext(integer.getAndIncrement());
            });
            thread.start();
        });
        Subject<Integer> subject = PublishSubject.create();
        subject.subscribe(integer1 -> System.out.println("[Main] value: " + integer1));
        subject.onNext(integer.getAndIncrement());
        subject.onNext(integer.getAndIncrement());
        subject.onNext(integer.getAndIncrement());
        subject.onNext(integer.getAndIncrement());
        subject.onNext(integer.getAndIncrement());
        flowable.subscribe(integer1 -> System.out.println("[" + Thread.currentThread() + "] value: " + integer1));
    }

    private static void d(Iterable<Integer> integers) {

    }

    private static class A<T> {
        List<T> value;

        void hello(List<T> list) {
            for (T t : list) {
                System.out.println(t);
            }
        }
    }

    private static class B extends A {
    }

}
