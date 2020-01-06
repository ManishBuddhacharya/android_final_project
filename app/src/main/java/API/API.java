package API;

import java.util.List;

import model.Candidate;
import model.ImageResponse;
import model.LoginSignupResponse;
import model.Position;
import model.User;
import model.Vote;
import model.VoteRes;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
    /*User*/
    @POST("user/signup")
    Call<LoginSignupResponse> signupUser(@Body User user);

    @POST("user")
    Call<LoginSignupResponse> insertUser(@Body User user);

    @POST("user/login")
    Call<LoginSignupResponse> loginUser(@Body User user);

/*position*/
    @POST("position")
    Call<LoginSignupResponse> insertPosition(@Header ("authorization") String token, @Body Position position);

    @GET("position")
    Call<List<Position>> fetchPositions();

    @GET("position/{posID}")
    Call<Position> searchPosition(@Header ("authorization") String token, @Path("posID") int posID);

    @PUT("position/{posID}")
    Call<LoginSignupResponse> updatePosition(@Header ("authorization") String token, @Path("posID") int posID, @Body Position position);

    @DELETE("position/{posID}")
    Call<LoginSignupResponse> deletePosition(@Header ("authorization") String token, @Path("posID") int posID);

    /*Members*/

    @GET("user")
    Call<List<User>> fetchUsers();

    @GET("user/{empID}")
    Call<User> searchUser(@Header ("authorization") String token, @Path("empID") int empID);

    @PUT("user/{empID}")
    Call<LoginSignupResponse> updateUser(@Header ("authorization") String token, @Path("empID") int empID, @Body User user);

    @DELETE("user/{empID}")
    Call<LoginSignupResponse> deleteUser(@Header ("authorization") String token, @Path("empID") int empID);

    /*Candidate Routes*/
	@GET("candidate")
    Call<List<Candidate>> fetchCandidates();

    @POST("candidate")
    Call<LoginSignupResponse> insertCandidate(@Header ("authorization") String token, @Body Candidate candidate);

    @GET("candidate/{candidateID}")
    Call<Candidate> searchCandidate(@Header ("authorization") String token, @Path("candidateID") int candidateID);

    @PUT("candidate/{candidateID}")
    Call<LoginSignupResponse> updateCandidate(@Header ("authorization") String token, @Path("candidateID") int candidateID, @Body Candidate candidate);

    @DELETE("candidate/{candidateID}")
    Call<LoginSignupResponse> deleteCandidate(@Header ("authorization") String token, @Path("candidateID") int candidateID);

    /*Vote*/
    @GET("android/position/candidate/{posID}/{voterID}")
    Call<List<Vote>> fetchPositionCandidatesAndroid(@Header ("authorization") String token, @Path("posID") int posID, @Path("voterID") int voterID);

    @PUT("vote/candidate/{candidateID}")
    Call<LoginSignupResponse> castVote(@Header ("authorization") String token, @Path("candidateID") int candidateID);

    @POST("vote/candidate")
    Call<LoginSignupResponse> castVoteHistory(@Header ("authorization") String token, @Body Vote vote);

    /*Result*/
    @GET("result/{posID}")
    Call<List<VoteRes>> fetchResult(@Header ("authorization") String token, @Path("posID") int posID);

    /*profile*/
    @GET("profile/{empID}")
    Call<User> searchProfile(@Header ("authorization") String token, @Path("empID") int empID);

    @PUT("profile/android/{empID}")
    Call<Void> updateProfileAndroid(@Header ("authorization") String token, @Path("empID") int empID, @Body User user);

    @Multipart
    @POST("profile/upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);
}
