package com.xiaomai.geek.data.net;

import com.xiaomai.geek.data.module.Issue;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.response.Content;
import com.xiaomai.geek.data.net.response.SearchResultResp;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XiaoMai on 2017/4/24.
 */

/**
 * GitHub 接口的 per_page 默认为30
 */
public interface GitHubService {

    @Headers("Cache-Control: public, max-age=600")
    @GET("search/repositories")
    Observable<SearchResultResp> searchRepo(@Query("q") String key,
                                            @Query("sort") String sort,
                                            @Query("order") String order,
                                            @Query("page") int page,
                                            @Query("per_page") int pageSize);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}")
    Observable<User> getSingleUser(@Path("user") String user);

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/repos")
    Observable<ArrayList<Repo>> getMyRepos(@Query("sort") String sort,
                                           @Query("type") String type,
                                           @Query("page") int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/repos")
    Observable<ArrayList<Repo>> getUserRepos(@Path("name") String user,
                                             @Query("sort") String sort,
                                             @Query("page") int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/starred")
    Observable<ArrayList<Repo>> getMyStarredRepos(@Query("sort") String sort,
                                                  @Query("page") int page);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/starred")
    Observable<ArrayList<Repo>> getUserStarredRepos(@Path("name") String user,
                                                    @Query("sort") String sort,
                                                    @Query("page") int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}/following")
    Observable<ArrayList<User>> getUserFollowing(@Path("user") String user,
                                                 @Query("page") int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("user/following")
    Observable<ArrayList<User>> getMyFollowing(@Query("page") int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}/followers")
    Observable<ArrayList<User>> getUserFollowers(@Path("user") String user,
                                                 @Query("page") int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("user/followers")
    Observable<ArrayList<User>> getMyFollowers(@Query("page") int page);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("repos/{owner}/{name}")
    Observable<Repo> get(@Path("owner") String owner, @Path("name") String repo);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("repos/{owner}/{name}/contributors")
    Observable<ArrayList<User>> contributors(@Path("owner") String owner,
                                             @Path("name") String repo);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("repos/{owner}/{name}/readme")
    Observable<Content> readme(@Path("owner") String owner, @Path("name") String repo);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("repos/{owner}/{name}/forks")
    Observable<ArrayList<Repo>> listForks(@Path("owner") String owner,
                                          @Path("name") String repo,
                                          @Query("sort") String sort);

    @GET("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkIfRepoIsStarred(@Path("owner") String owner,
                                                            @Path("repo") String repo);

    @Headers("Content-Length: 0")
    @PUT("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> starRepo(@Path("owner") String owner,
                                                @Path("repo") String repo);

    @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unStarRepo(@Path("owner") String owner,
                                                  @Path("repo") String repo);

    @POST("repos/{owner}/{repo}/issues")
    Observable<Response<ResponseBody>> createIssue(@Path("owner") String owner,
                                                   @Path("repo") String repo,
                                                   @Body Issue issue);
}
