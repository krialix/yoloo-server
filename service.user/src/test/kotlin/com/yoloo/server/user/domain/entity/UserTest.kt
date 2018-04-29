package com.yoloo.server.user.domain.entity

import com.google.common.truth.Truth.assertThat
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
            displayName = UserDisplayName("name", "name"),
            url = Url("http://url.com"),
            provider = SocialProvider("2", ProviderType.FACEBOOK),
            email = Email("test@test.com"),
            image = AvatarImage("http://url.com"),
            gender = Gender.MALE,
            lastKnownIP = IP("127.0.0.1"),
            fcmToken = "token",
            subscribedGroups = listOf(UserGroup("g1", "http://g1.com", "g1Name")),
            locale = UserLocale("en", "EN"),
            scopes = setOf("scope1", "scope2")
        )

        val loaded = ofy().saveClearLoad(original)

        assertThat(original.id).isEqualTo(loaded.id)
        assertThat(original.displayName).isEqualTo(loaded.displayName)
        assertThat(original.url).isEqualTo(loaded.url)
        assertThat(original.provider).isEqualTo(loaded.provider)
        assertThat(original.email).isEqualTo(loaded.email)
        assertThat(original.image).isEqualTo(loaded.image)
        assertThat(original.gender).isEqualTo(loaded.gender)
        assertThat(original.lastKnownIP).isEqualTo(loaded.lastKnownIP)
        assertThat(original.fcmToken).isEqualTo(loaded.fcmToken)
        assertThat(original.subscribedGroups).isEqualTo(loaded.subscribedGroups)
        assertThat(original.locale).isEqualTo(loaded.locale)
        assertThat(original.scopes).isEqualTo(loaded.scopes)

        assertThat(loaded.userFilterData).isNotNull()
        assertThat(loaded.createdAt).isNotNull()
        assertThat(loaded.updatedAt).isNull()
    }

    @Test
    fun updateUser_updatedAtGenerated_ShouldUpdatedAtNotNull() {
        val original = User(
            id = 1,
            displayName = UserDisplayName("name", "name"),
            url = Url("http://url.com"),
            provider = SocialProvider("2", ProviderType.FACEBOOK),
            email = Email("test@test.com"),
            image = AvatarImage("http://url.com"),
            gender = Gender.MALE,
            lastKnownIP = IP("127.0.0.1"),
            fcmToken = "token",
            subscribedGroups = listOf(UserGroup("g1", "http://g1.com", "g1Name")),
            locale = UserLocale("en", "EN"),
            scopes = setOf("scope1", "scope2")
        )

        val loaded = ofy().saveClearLoad(original)
        val loaded2 = ofy().saveClearLoad(loaded)

        assertThat(loaded2.updatedAt).isNotNull()
    }
}