package dev.sukh.learnkotlin.extensions

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.Intent.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sukh.learnkotlin.R
import dev.sukh.learnkotlin.app.App
import dev.sukh.learnkotlin.interfaces.OnConfirmation
import dev.sukh.learnkotlin.interfaces.OnMenuSelection
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.dialog_confirmation.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit


var progressDialog: Dialog? = null
fun Context.launchProgress() {
    progressDialog = Dialog(this)
    progressDialog?.setContentView(R.layout.dialog_progress)
    progressDialog?.setCancelable(false)
    progressDialog?.setCanceledOnTouchOutside(false)
    progressDialog?.show()
    progressDialog?.show()
}

fun Context.disposeProgress() {
    if (progressDialog != null)
        progressDialog?.dismiss()
}

/**
 * Set an onclick listener
 */
fun View.click(block: () -> Unit) = setOnClickListener { block.invoke() }

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment? = null, @IdRes frameId: Int = R.id.container) {
    supportFragmentManager.transact {
        replace(frameId, fragment!!)
    }
}


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}


/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */

fun Fragment.addFragmentToFragment(fragment: Fragment? = null, @IdRes frameId: Int = R.id.container) {
    fragmentManager?.transact {
        add(frameId, fragment!!)
    }
}


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */

fun Fragment.replaceFragmentInFragment(fragment: Fragment? = null, @IdRes frameId: Int = R.id.container) {
    fragmentManager?.transact {
        replace(frameId, fragment!!)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment? = null, @IdRes frameId: Int = R.id.container) {
    supportFragmentManager.transact {
        add(frameId, fragment!!)
    }
}

fun View.menu(@MenuRes menu: Int, listner: OnMenuSelection) {

    val popupMenu: PopupMenu = PopupMenu(context, this)
    popupMenu.menuInflater.inflate(menu, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener({ item ->
        listner.onClickMenu(item)
        true
    })
    popupMenu.show()
}

fun screenSize(): DisplayMetrics {
    var displayMetrics = DisplayMetrics()
    return displayMetrics
}

/**
 * Extension method to set View's width.
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * Hide the view. (visibility = View.VISIBLE)
 */
fun View.visible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.pressBack() {
    this.click {
        var ac = this.context as Activity
        ac.onBackPressed()
    }
}

/**
 * Extension method to share for Context.
 */
fun Context.share(text: String = "", subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    try {
        startActivity(createChooser(intent, subject))
        return true
    } catch (e: ActivityNotFoundException) {
        toast(e.message!!)
        return false
    }
}


/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)


fun EditText.focus(text: TextView) {
    onFocusChangeListener = object : View.OnFocusChangeListener {
        override fun onFocusChange(p0: View?, p1: Boolean) {
            if (p1)
                text.setTextColor(p0?.context?.getColorCompat(R.color.colorPrimary)!!)
            else
                text.setTextColor(p0?.context?.getColorCompat(R.color.black)!!)
        }

    }
}

/**
 * Extension method to provide manager to Recycler View.
 */

fun RecyclerView.setManager(isItHorizontal: Boolean = false, isItGrid: Boolean = false) {
    if (isItGrid)
        this.layoutManager = GridLayoutManager(this.context, 2) as RecyclerView.LayoutManager?
    else {
        if (isItHorizontal)
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        else
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }


}

// Copy EditCopy text to the ClipBoard
fun copyToClipBoard(text: String, context: Context) {
    var clipMan = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(text, text)
    clipMan.setPrimaryClip(clip)
    toast("$text Copied")
}


/**
 * Create an intent for [T] and apply a lambda on it
 */
inline fun <reified T : Any> Context.launchActivity(body: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.body()
    startActivity(intent)
}


fun Context.dialog(@LayoutRes layout: Int, listner: OnConfirmation) {
    var dialog = Dialog(this)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialog.setContentView(layout)
    dialog.txtCancel.click { dialog.dismiss() }
    dialog.txtDiscard.click {
        dialog.dismiss()
        listner.onConfirm()
    }
    dialog.show()
}


fun AppCompatActivity.launchCamera(requestCode: Int) {
    var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(cameraIntent, requestCode)
}

/**
 * Extension method to save Bitmap to specified file path.
 */
fun Bitmap.saveFile(path: String): Boolean {
    try {
        val f = File(path)
        if (!f.exists()) {
            f.createNewFile()
        }
        val stream = FileOutputStream(f)
        compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        return true
    } catch (a: Exception) {
        toast(a.message!!)
        return false
    }
}

/**
 * Extension method to image picker from gallery.
 */

fun ImageView.load(path: Any) {
    Glide.with(this.context)
        .load(path)
        .apply(RequestOptions().placeholder(R.drawable.img_default))
        .into(this)
}

fun AppCompatActivity.pickImage(requestCode: Int) {
    var ar = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    if (!isPermissionsGranted(this, ar))
        askForPermissions(this, ar)
    else {
        val galleryIntent =
            Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        // Start the Intent
        startActivityForResult(galleryIntent, requestCode)
    }
}

/**
 * Extension method to set View's height.
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

/**
 * Extension method underLine for TextView.
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * Extension method to check if String is Email.
 */
fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.contains("com")
}


/**
 * Extension method to get value from EditText.
 */
val EditText.value
    get() = text.toString()


/**
 * Extension method to run block of code after specific Delay.
 */
fun runDelayed(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    Handler().postDelayed(action, timeUnit.toMillis(delay))
}


/**
 * Extension method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())


/**
 * Extension method to check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}


fun toast(message: String) {
    Toast.makeText(App.application.applicationContext, message, Toast.LENGTH_LONG).show()
}

fun showLog(message: String, title: String) {
    Log.e(title, message)
}


fun isPermissionsGranted(context: Context, p0: Array<String>): Boolean {
    p0.forEach {
        if (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED)
            return false
    }
    return true
}

fun askForPermissions(activity: Activity, array: Array<String>) {
    ActivityCompat.requestPermissions(activity, array, 0)
}


fun showConfirmAlert(
    message: String?, positiveText: String?
    , negativetext: String?
    , onConfirmed: () -> Unit = {}
    , onCancel: () -> Unit = { }
) {

    if (message.isNullOrEmpty()) return

    val builder = AlertDialog.Builder(App.application.applicationContext)

    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveText) { dialog, id ->
            //do things

            onConfirmed.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativetext) { dialog, id ->
            //do things

            onCancel.invoke()
            dialog.dismiss()
        }

    val alert = builder.create()
    alert.show()
    alert.getButton(Dialog.BUTTON_NEGATIVE).isAllCaps = false
    alert.getButton(Dialog.BUTTON_POSITIVE).isAllCaps = false
}


fun getDateDifference(startDate: Long): String {
    var different = Calendar.getInstance().time.time - startDate

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val monthInMilli = hoursInMilli * 24 * 30
    val yearInMilli = hoursInMilli * 24 * 30 * 12

    val years = different / yearInMilli
    different %= yearInMilli

    val months = different / monthInMilli
    different %= monthInMilli

    val days = different / daysInMilli
    different %= daysInMilli

    val hours = different / hoursInMilli
    different %= hoursInMilli

    val minutes = different / minutesInMilli

    if (years > 1)
        return "$years years ago"
    else if (years == 1.toLong())
        return "a year ago"
    else if (months > 1)
        return "$months months ago"
    else if (months == 1.toLong())
        return "a month ago"
    else if (days > 1)
        return "$days days ago"
    else if (days == 1.toLong())
        return "a day ago"
    else if (hours > 1)
        return "$hours hours ago"
    else if (hours == 1.toLong())
        return "a hour ago"
    else if (minutes > 1)
        return "$minutes minutes ago"
    else
        return "few seconds ago"
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
