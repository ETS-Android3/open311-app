package com.iu.open311.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StatusTranslaterTest {
    @Mock
    private Resources resources;

    @Test
    public void determineStatus() {
        List<String> availableStatus =
                Arrays.asList("pending", "received", "in_process", "reviewed", "processed",
                        "rejected", "closed"
                );

        availableStatus.forEach(statusKey -> {
            String translatedStatus = "translated state: " + statusKey;
            when(resources.getString(anyInt())).thenReturn(translatedStatus);

            String determinedStatus = StatusTranslater.determineStatus(resources, statusKey);
            assertEquals(translatedStatus, determinedStatus);
        });
    }

    @Test
    public void determineStatusDefaultValue() {
        String statusKey = "anyKey";
        assertSame(statusKey, StatusTranslater.determineStatus(resources, statusKey));

        verify(resources, never()).getString(anyInt());
    }
}