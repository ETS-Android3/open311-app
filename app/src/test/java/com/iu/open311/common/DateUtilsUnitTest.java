package com.iu.open311.common;


import androidx.core.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateUtilsUnitTest {

    @Test
    public void testFormatLocalDateTime() {
        List<Pair<LocalDateTime, String>> testSet = new ArrayList<>();
        testSet.add(Pair.create(LocalDateTime.of(1970, 1, 1, 0, 0), "01.01.1970, 00:00:00"));
        testSet.add(Pair.create(LocalDateTime.of(2020, 2, 29, 12, 0), "29.02.2020, 12:00:00"));
        testSet.add(
                Pair.create(LocalDateTime.of(2021, 10, 19, 18, 28, 35), "19.10.2021, 18:28:35"));

        testSet.forEach(pair -> {
            Assert.assertEquals(pair.second, DateUtils.formatLocalDateTime(pair.first));
        });
    }
}