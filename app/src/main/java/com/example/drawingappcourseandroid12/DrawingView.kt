package com.example.drawingappcourseandroid12

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
//    private val mPathsDummy = ArrayList<CustomPath>()


    init {
        setUpDrawing()
    }

    //Note I wrote it from scratch
    //.. To be published on GitHub
    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint?.color = color
        mDrawPaint?.style = Paint.Style.STROKE
        mDrawPaint?.strokeJoin = Paint.Join.ROUND
        mDrawPaint?.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
//        mBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //Change Canvas to Canvas? to avoid null pointer exception
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (path in mPaths) {
            mDrawPaint?.strokeWidth = path.brushThickness
            mDrawPaint?.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint?.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint?.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = color
                mDrawPath?.brushThickness = mBrushSize

                mDrawPath?.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath?.moveTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath?.lineTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mDrawPath = CustomPath(color, mBrushSize)
                mPaths.add(mDrawPath!!)
//                canvas?.drawPath(mDrawPath!!, mDrawPaint!!)
//                mDrawPath?.reset()
            }
            else -> return false
        }
        invalidate()

        return true

    }

    fun redo() {
        if (mPaths.size > 0) {
/*
            mPaths.add(mPathsDummy[0])
*/
/*
            mPathsDummy.removeAt(mPathsDummy.size - 1)
*/
            invalidate()
        }
    }



    fun undo() {
        if (mPaths.size > 0) {
/*
            mPathsDummy.add(mPaths[mPaths.size - 1])
*/
            mPaths.removeAt(mPaths.size - 1)
            invalidate()
        }
    }

    fun clear() {
        mPaths.clear()
        invalidate()
    }

    fun setSizeForBrush(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )
        mDrawPaint?.strokeWidth = mBrushSize
    }

    fun setColor(it: Int) {
        color = it
    }


    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }

}

