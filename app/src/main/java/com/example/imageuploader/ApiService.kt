import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("upload_image.php")  // Ensure this matches your actual API endpoint
    fun uploadImage(
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UploadResponse>

    @GET("get_images.php") // Corrected API endpoint
    fun getUploadedImages(@Query("category") category: String): Call<List<String>>
}
