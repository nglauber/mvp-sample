package br.com.nglauber.exemplolivro.unit

import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import br.com.nglauber.exemplolivro.model.data.Post
import br.com.nglauber.exemplolivro.model.persistence.web.PostWeb
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TestPostsWeb {

    private val context by lazy { InstrumentationRegistry.getTargetContext() }
    private lateinit var postWeb: PostWeb

    @Before
    fun setup() {
        postWeb = PostWeb("testUser", context)
    }

    @Test
    fun crudTest() {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.MILLISECOND, 0)

        val post = Post(0, "Test insert", calendar.time, getRandomPictureFromGallery(), 1.0, 2.0)

        // insert
        val saveSubscriber = postWeb.savePost(post).test()
        val postId = saveSubscriber.values().first()
        assert(postId != 0L)
        post.id = postId

        // update
        post.text = "Post updated"
        postWeb.savePost(post).test().assertValue(postId)

        // retrieve post
        val loadSubscriber = postWeb.loadPost(postId).test()
        loadSubscriber.assertValue(post)

        // delete post
        val deleteSubscriber = postWeb.deletePost(post).test()
        deleteSubscriber.assertResult(true)
    }

    private fun getRandomPictureFromGallery() : String? {
        // run this command before
        // adb shell pm grant br.com.nglauber.exemplolivro android.permission.READ_EXTERNAL_STORAGE
        val columns = arrayOf( ImageColumns._ID )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(uri, columns, null, null, null)
        var uriResult : String? = null
        val size = cursor.count
        if (size > 0){
            cursor.moveToFirst()

            val position = Random().nextInt(size)
            cursor.move(position)

            uriResult = "${uri}/${cursor.getLong(0)}"
        }
        cursor.close()
        return uriResult
    }
}