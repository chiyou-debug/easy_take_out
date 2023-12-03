package com.easy.utils;

import com.easy.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BeanHelper {

    /**
     * Copy properties from a source object to a target object of a given class.
     *
     * @param source Source object
     * @param target Target class
     * @param <T>    Type of the target class
     * @return New instance of the target class with copied properties
     */
    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            if (source == null) return null;
            T t = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("Error in data conversion, constructor exception in target object {}", target.getName(), e);
            throw new RuntimeException(MessageConstant.DATA_TRANSFER_ERROR);
        }
    }

    /**
     * Copy properties from a source object to a target object of a given class, ignoring specified properties.
     *
     * @param source           Source object
     * @param target           Target class
     * @param ignoreProperties Properties to ignore during copying
     * @param <T>              Type of the target class
     * @return New instance of the target class with copied properties
     */
    public static <T> T copyProperties(Object source, Class<T> target, String... ignoreProperties) {
        try {
            if (source == null) return null;
            T t = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            log.error("Error in data conversion, constructor exception in target object {}", target.getName(), e);
            throw new RuntimeException(MessageConstant.DATA_TRANSFER_ERROR);
        }
    }

    /**
     * Copy a collection of source objects to a collection of target class objects.
     *
     * @param sourceList Source collection
     * @param target     Target class
     * @param <T>        Type of the target class
     * @return New collection of the target class with copied properties
     */
    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target) {
        try {
            if (sourceList == null) return null;
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in data conversion, constructor exception in target object {}", target.getName(), e);
            throw new RuntimeException(MessageConstant.DATA_TRANSFER_ERROR);
        }
    }

    /**
     * Copy a set of source objects to a set of target class objects.
     *
     * @param sourceList Source set
     * @param target     Target class
     * @param <T>        Type of the target class
     * @return New set of the target class with copied properties
     */
    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target) {
        try {
            if (sourceList == null) return null;
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Error in data conversion, constructor exception in target object {}", target.getName(), e);
            throw new RuntimeException(MessageConstant.DATA_TRANSFER_ERROR);
        }
    }
}
