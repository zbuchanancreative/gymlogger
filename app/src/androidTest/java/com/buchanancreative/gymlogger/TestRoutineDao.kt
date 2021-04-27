package com.buchanancreative.gymlogger

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.buchanancreative.loggerlibrary.Manager.Dao.RoutineDao
import com.buchanancreative.loggerlibrary.Model.Routine
import com.buchanancreative.loggerlibrary.Persistence.LoggingDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoutineTest {
    private lateinit var routineDao: RoutineDao
    private lateinit var db: LoggingDatabase

    private val routine1 = Routine(1, "Routine 1")
    private val routine2 = Routine(2, "Routine 2")
    private val routine3 = Routine(3, "Routine 3")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, LoggingDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        routineDao = db.routineDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertingRoutines() {
        routineDao.insert(routine1)
        routineDao.insert(routine2)
        routineDao.insert(routine3)

        val routines =  routineDao.getAllRoutines()

        assertEquals(3, getValue(routines).size)
    }

    @Test
    fun testDeletingRoutines() {
        routineDao.insert(routine1)
        routineDao.insert(routine2)
        routineDao.insert(routine3)

        routineDao.deleteRoutine(routine2)
        routineDao.deleteRoutine(routine1)

        val routines = routineDao.getAllRoutines()

        assertEquals(1, getValue(routines).size)
        assertEquals("Routine 3", routines.value!![0].name)
    }

    @Test
    fun testRoutineUpdate() {
        routineDao.insert(routine1)

        routine1.name = "NEW STUFF"
        routineDao.updateRoutine(routine1)

        val routines = routineDao.getAllRoutines()

        assertEquals("NEW STUFF", getValue(routines)[0].name)
    }
}