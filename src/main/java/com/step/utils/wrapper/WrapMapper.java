package com.step.utils.wrapper;

/**
 * Created by zhushubin  on 2019-05-21.
 * email:604580436@qq.com
 */
public class WrapMapper {

    /**
     * Instantiates a new wrap mapper.
     */
    private WrapMapper() {
    }

    /**
     * Wrap.
     *
     * @param <E>     the element type
     * @param type    响应数据类型
     * @param message the message
     * @param o       the o
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> wrap(Wrapper.ResponseType type, String message, E o) {
        return new Wrapper<>(type, message, o);
    }

    /**
     * Wrap.
     *
     * @param <E>     the element type
     * @param type    the code
     * @param message the message
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> wrap(Wrapper.ResponseType type, String message) {
        return wrap(type, message, null);
    }

    /**
     * Wrap.
     *
     * @param <E>  the element type
     * @param type 响应数据类型
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> wrap(Wrapper.ResponseType type) {
        return wrap(type, null);
    }

    /**
     * Wrap.
     *
     * @param <E> the element type
     * @param e   the e
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> wrap(Exception e) {
        return new Wrapper<>(Wrapper.ResponseType.ERROR, e.getMessage());
    }

    /**
     * Un wrapper.
     *
     * @param <E>     the element type
     * @param wrapper the wrapper
     *
     * @return the e
     */
    public static <E> E unWrap(Wrapper<E> wrapper) {
        return wrapper.getResult();
    }

    /**
     * Wrap ERROR. code=100
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> illegalArgument() {
        return wrap(Wrapper.ResponseType.ILLEGAL_ARGUMENT);
    }

    /**
     * Wrap ERROR. code=500
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> error() {
        return wrap(Wrapper.ResponseType.ERROR);
    }


    /**
     * Error wrapper.
     *
     * @param <E>     the type parameter
     * @param message the message
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> error(String message) {
        return wrap(Wrapper.ResponseType.ERROR, message);
    }

    /**
     * Wrap SUCCESS. code=200
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> ok() {
        return new Wrapper<>();
    }

    /**
     * Ok wrapper.
     *
     * @param <E> the type parameter
     * @param o   the o
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> ok(E o) {
        return new Wrapper<>(Wrapper.ResponseType.SUCCESS,o);
    }
}

