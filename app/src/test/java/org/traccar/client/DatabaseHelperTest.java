
package com.chungauto.tracking;

import android.location.Location;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.chungauto.tracking.DatabaseHelper;
import com.chungauto.tracking.Position;

@Config(sdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperTest {

    @Test
    public void test() throws Exception {

        DatabaseHelper databaseHelper = new DatabaseHelper(RuntimeEnvironment.application);

        Position position = new Position("123456789012345", new Location("gps"), 0);
        position.setTime(new Date(0));

        assertNull(databaseHelper.selectPosition());

        databaseHelper.insertPosition(position);

        position = databaseHelper.selectPosition();

        assertNotNull(position);

        databaseHelper.deletePosition(position.getId());

        assertNull(databaseHelper.selectPosition());

    }

}
