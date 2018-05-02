package com.yoloo.server.user.domain.entity

import com.google.common.truth.Truth.assertThat
import com.yoloo.server.common.vo.Url
import com.yoloo.server.objectify.translators.LocalDateTimeDateTranslatorFactory
import com.yoloo.server.user.domain.vo.*
import com.yoloo.server.user.infrastructure.objectify.translators.CuckooFilterTranslatorFactory
import com.yoloo.server.user.infrastructure.social.ProviderType
import com.yoloo.server.util.AppEngineRule
import com.yoloo.server.util.TestObjectifyService.fact
import com.yoloo.server.util.TestObjectifyService.ofy
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserTest {

    @get:Rule
    val appEngineRule: AppEngineRule =
        AppEngineRule.builder().withDatastore().withMemcacheService().build()

    @Before
    fun setUp() {
        fact().translators.add(LocalDateTimeDateTranslatorFactory())
        fact().translators.add(CuckooFilterTranslatorFactory())
        fact().register(User::class.java)
    }

    @Test
    fun createNewUser_AllFieldsValid_ShouldEqualWhenLoadedFromDb() {
        val original = User(
            id = 1,
            profile = Profile(
                displayName = UserDisplayName("name"),
                image = AvatarImage(Url("http://url.com")),
                gender = Gender.MALE,
                locale = UserLocale("en", "EN")
            ),
            account = Account(
                username = Username("username"),
                provider = SocialProvider("2", ProviderType.FACEBOOK),
                email = Email("test@test.com"),
                lastKnownIP = IP("127.0.0.1"),
                fcmToken = "providerIdToken",
                scopes = setOf("scope1", "scope2")
            ),
            subscribedGroups = listOf(UserGroup(1L, "http://g1.com", "g1Name"))
        )

        val loaded = ofy().saveClearLoad(original)

        assertThat(original.id).isEqualTo(loaded.id)
        assertThat(original.profile.displayName).isEqualTo(loaded.profile.displayName)
        assertThat(original.profile.image.url).isEqualTo(loaded.profile.image.url)
        assertThat(original.profile.image).isEqualTo(loaded.profile.image)
        assertThat(original.profile.gender).isEqualTo(loaded.profile.gender)
        assertThat(original.profile.locale).isEqualTo(loaded.profile.locale)
        assertThat(original.account.provider).isEqualTo(loaded.account.provider)
        assertThat(original.account.email).isEqualTo(loaded.account.email)
        assertThat(original.account.lastKnownIP).isEqualTo(loaded.account.lastKnownIP)
        assertThat(original.account.fcmToken).isEqualTo(loaded.account.fcmToken)
        assertThat(original.account.scopes).isEqualTo(loaded.account.scopes)
        assertThat(original.subscribedGroups).isEqualTo(loaded.subscribedGroups)

        assertThat(loaded.userFilterData).isNotNull()
        assertThat(loaded.createdAt).isNotNull()
        assertThat(loaded.updatedAt).isNull()
    }

    @Test
    fun updateUser_updatedAtGenerated_ShouldUpdatedAtNotNull() {
        val original = User(
            id = 1,
            profile = Profile(
                displayName = UserDisplayName("name"),
                image = AvatarImage(Url("http://url.com")),
                gender = Gender.MALE,
                locale = UserLocale("en", "EN")
            ),
            account = Account(
                username = Username("username"),
                provider = SocialProvider("2", ProviderType.FACEBOOK),
                email = Email("test@test.com"),
                lastKnownIP = IP("127.0.0.1"),
                fcmToken = "providerIdToken",
                scopes = setOf("scope1", "scope2")
            ),
            subscribedGroups = listOf(UserGroup(1L, "http://g1.com", "g1Name"))
        )

        val loaded = ofy().saveClearLoad(original)
        val loaded2 = ofy().saveClearLoad(loaded)

        assertThat(loaded2.updatedAt).isNotNull()
    }
}