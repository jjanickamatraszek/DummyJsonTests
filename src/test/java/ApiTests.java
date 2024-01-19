import auth.login.PostUserCredentialsMethod;
import com.zebrunner.agent.core.annotation.TestCaseKey;
import com.zebrunner.carina.api.APIMethodPoller;
import com.zebrunner.carina.api.apitools.validation.JsonComparatorContext;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.DeletePostRs;
import model.GetAllPostsRs;
import model.PostRs;
import model.SearchPostsRs;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import posts.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiTests implements IAbstractTest {
    private String userId;

    @BeforeClass
    public void before() {
        PostUserCredentialsMethod postCreds = new PostUserCredentialsMethod();
        JsonPath jsonPath = postCreds.callAPI().jsonPath();
        userId = jsonPath.getString("id");
    }

    @DataProvider(name = "get incorrect user ids")
    public Object[][] getIncorrectUserIds() {
        return new Object[][]{
                {"10000000", "error.userNotFound.message", HttpResponseStatusType.NOT_FOUND_404},
                {"0", "error.userNotFound.message", HttpResponseStatusType.NOT_FOUND_404},
                {"-1", "error.userNotFound.message", HttpResponseStatusType.NOT_FOUND_404},
                {"aa", "error.userIncorrect.message", HttpResponseStatusType.BAD_REQUEST_400}
        };
    }

    @Test
    @TestCaseKey(value = "JOANNA-57")
    public void addPostWithFullInfoTest() {
        AddPostMethod addPostMethod = new AddPostMethod(userId);
        addPostMethod.callAPIExpectSuccess();
        addPostMethod.validateResponse();
        addPostMethod.validateResponseAgainstSchema("api/posts/addPost/rs.schema");
    }

    @Test
    @TestCaseKey(value = "JOANNA-58")
    public void getPostTest() {
        GetPostMethod getPostMethod = new GetPostMethod();
        getPostMethod.callAPIExpectSuccess();
        getPostMethod.validateResponse();
        getPostMethod.validateResponseAgainstSchema("api/posts/getPost/rs.schema");
    }

    @Test
    @TestCaseKey(value = "JOANNA-59")
    public void updatePostTitleTest() {
        String updatedTitle = "Updated title";

        UpdatePostMethod updatePostMethod = new UpdatePostMethod();
        updatePostMethod.getProperties().setProperty("title", updatedTitle);
        updatePostMethod.callAPIExpectSuccess();
        updatePostMethod.validateResponse();
        updatePostMethod.validateResponseAgainstSchema("api/posts/addPost/rs.schema");
    }

    @Test(description = "Test with response validation using JSON template")
    @TestCaseKey(value = "JOANNA-60")
    public void deletePost_v1Test() {
        DeletePostMethod deletePostMethod = new DeletePostMethod();
        JsonComparatorContext comparatorContext = JsonComparatorContext.context()
                .<String>withPredicate("todayDatePredicate", date -> date.startsWith(LocalDate.now().toString()));
        deletePostMethod.callAPIExpectSuccess();
        deletePostMethod.validateResponse(comparatorContext);
        deletePostMethod.validateResponseAgainstSchema("api/posts/deletePost/rs.schema");
    }

    @Test(description = "Test with response validation using POJO")
    @TestCaseKey(value = "JOANNA-61")
    public void deletePost_v2Test() {
        GetAllPostsMethod getAllPostsMethod = new GetAllPostsMethod();
        GetAllPostsRs allPostAsList = getAllPostsMethod.callAPI().as(GetAllPostsRs.class);

        PostRs postToDelete = allPostAsList.getPosts().get(0);
        DeletePostMethod deletePostMethod = new DeletePostMethod(postToDelete.getId());
        DeletePostRs deletePostRs = deletePostMethod.callAPIExpectSuccess().as(DeletePostRs.class);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(deletePostRs, postToDelete,
                "Deleted post entries are different from expected post");
        soft.assertTrue(deletePostRs.isDeleted(),
                "Status for isDeleted is set to false");
        soft.assertTrue(deletePostRs.getDeletedOn().startsWith(LocalDate.now().toString()),
                "Delete date doesn't start with today's date - '%s'. It's '%s'".formatted(LocalDate.now().toString(), deletePostRs.getDeletedOn()));
        soft.assertAll();
    }

    @Test
    @TestCaseKey(value = "JOANNA-62")
    public void getAllPostsLimitedWithSkipTest() {
        int expectedLimit = 5;
        int expectedSkip = 10;

        GetAllPostsMethod getPostsFromBeginningMethod = new GetAllPostsMethod(expectedLimit);
        GetAllPostsRs getPostsFromBeginningRs = getPostsFromBeginningMethod.callAPI().as(GetAllPostsRs.class);
        GetAllPostsMethod getPostsWithSkipMethod = new GetAllPostsMethod(expectedLimit, expectedSkip);
        GetAllPostsRs getPostsWithSkipRs = getPostsWithSkipMethod.callAPIExpectSuccess().as(GetAllPostsRs.class);
        getPostsWithSkipMethod.validateResponseAgainstSchema("api/posts/getAllPosts/rs.schema");
        SoftAssert soft = new SoftAssert();
        soft.assertNotEquals(getPostsWithSkipRs.getPosts(), getPostsFromBeginningRs.getPosts(),
                "Posts from request with skip param are the same as posts without");
        soft.assertEquals(getPostsWithSkipRs.getPosts().size(), expectedLimit,
                "Amount of post returned is different than value set for limit in request");
        soft.assertEquals(getPostsWithSkipRs.getSkip(), expectedSkip,
                "Value for skip in response is different than set in request");
        soft.assertEquals(getPostsWithSkipRs.getLimit(), expectedLimit,
                "Value for limit in response is different than set in request");
        soft.assertAll();
    }

    @Test
    @TestCaseKey(value = "JOANNA-63")
    public void getAllPostsOfUserTest() {
        int expectedPostAmount = 6;

        GetAllPostsByUserIdMethod allPostsByUserId = new GetAllPostsByUserIdMethod(userId);
        Response response = allPostsByUserId.callAPIExpectSuccess();
        SoftAssert soft = new SoftAssert();
        List<Integer> userIds = response.jsonPath().getList("posts.userId");
        soft.assertEquals(userIds.size(), expectedPostAmount,
                "Amount of posts added by user with id %s is different than expected".formatted(userId));
        Set<Integer> uniqueUserIds = new HashSet<>(userIds);
        soft.assertEquals(uniqueUserIds.size(), 1,
                "Posts belong to more than one user");
        soft.assertEquals(uniqueUserIds.stream().findFirst().orElse(-1), Integer.valueOf(userId),
                "Posts belong to user whose id is different than supplied in request");
        soft.assertAll();
        allPostsByUserId.validateResponseAgainstSchema("api/posts/getAllPosts/rs.schema");
    }

    @Test(dataProvider = "get incorrect user ids")
    @TestCaseKey(value = "JOANNA-64")
    public void dontGetPostsOfNotExistingUser(String incorrectUserId, String message, HttpResponseStatusType statusType) {
        GetAllPostsByUserIdMethod allPostsByUserId = new GetAllPostsByUserIdMethod(incorrectUserId);
        allPostsByUserId.expectResponseStatus(statusType);
        String expectedErrorMessage = allPostsByUserId.getProperties().getProperty(message).formatted(incorrectUserId);
        Response response = allPostsByUserId.callAPI();
        String actualErrorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage,
                "Error message is different than expected");
    }

    @Test
    @TestCaseKey(value = "JOANNA-65")
    public void searchPostsBySinglePhraseTest() {
        String searchPhrase = "good";

        SearchPostsMethod searchPostsMethod = new SearchPostsMethod(searchPhrase);
        SearchPostsRs searchPostsRs = searchPostsMethod.callAPIExpectSuccess().as(SearchPostsRs.class);
        searchPostsMethod.validateResponseAgainstSchema("api/posts/getAllPosts/rs.schema");
        int amountOfReturnedPosts = searchPostsRs.getPosts().size();
        Assert.assertTrue(searchPostsRs.getPosts().size() > 0,
                "No posts were returned for phrase '%s'".formatted(searchPhrase));
        long amountOfPostsContainingSearchPhrase = searchPostsRs
                .getPosts()
                .stream().map(p -> p.getBody())
                .filter(p -> p.toLowerCase().contains(searchPhrase))
                .count();
        Assert.assertEquals(amountOfPostsContainingSearchPhrase, amountOfReturnedPosts,
                "Amount of post returned is different than amount of posts containing search phrase '%s' in body".formatted(searchPhrase));
    }

    @Test
    @TestCaseKey(value = "JOANNA-66")
    public void getDefaultAllPostResponseWhenLimitAndSkipAreNegativeTest() {
        GetAllPostsMethod getAllPostsMethod = new GetAllPostsMethod();
        GetAllPostsRs expectedGetAllPostsRs = getAllPostsMethod.callAPI().as(GetAllPostsRs.class);

        GetAllPostsMethod getAllPostsMethodWithNegativeValues = new GetAllPostsMethod(-10, -20);
        GetAllPostsRs actualGetAllPostsRs = getAllPostsMethodWithNegativeValues.callAPIExpectSuccess().as(GetAllPostsRs.class);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(actualGetAllPostsRs.getPosts(), expectedGetAllPostsRs.getPosts(),
                "List of posts from request with negative values is different than from request without any params");
        soft.assertEquals(actualGetAllPostsRs.getLimit(), R.TESTDATA.get("posts.defaultLimit"),
                "Limit in response isn't set to default value");
        soft.assertEquals(actualGetAllPostsRs.getLimit(), R.TESTDATA.get("posts.defaultSkip"),
                "Skip in response isn't set to default value");
        soft.assertAll();
    }

    @Test
    @TestCaseKey(value = "JOANNA-67")
    public void getSelectedEntriesFromGetAllPostTest() {
        List<String> selectedEntries = List.of(R.TESTDATA.get("post.entry_1"), R.TESTDATA.get("post.entry_2"), R.TESTDATA.get("post.entry_3"));
        GetAllPostsMethod getAllPostsMethodSelected = new GetAllPostsMethod(selectedEntries);

        AtomicInteger counter = new AtomicInteger(0);
        getAllPostsMethodSelected.callAPIWithRetry()
                .withLogStrategy(APIMethodPoller.LogStrategy.ALL)
                .peek(rs -> counter.getAndIncrement())
                .until(rs -> rs.getBody().asString().contains(R.TESTDATA.get("post.entry_1")) || counter.get() == 4)
                .pollEvery(1, ChronoUnit.SECONDS)
                .stopAfter(6, ChronoUnit.SECONDS)
                .execute();
        getAllPostsMethodSelected.validateResponseAgainstSchema("api/posts/getAllPosts/selected_rs.schema");
    }
}
