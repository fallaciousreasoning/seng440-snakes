package nz.ac.canterbury.csse.a440.snakes;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A gesture listener that passes gestures to listeners
 */
public class AggregateGestureListener implements GestureDetector.OnGestureListener {
    private Collection<GestureDetector.OnGestureListener> listeners = new ArrayList<>();

    /**
     * Adds a listener to the gesture listener
     * @param listener The listener
     */
    public void addGestureListener(GestureDetector.OnGestureListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            if (listener.onDown(e)) return true;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            listener.onShowPress(e);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            if (listener.onSingleTapUp(e)) return true;
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            if (listener.onScroll(e1, e2, distanceX, distanceY)) return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            listener.onLongPress(e);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        for (GestureDetector.OnGestureListener listener: listeners) {
            if (listener.onFling(e1, e2, velocityX, velocityY)) return true;
        }
        return false;
    }
}
