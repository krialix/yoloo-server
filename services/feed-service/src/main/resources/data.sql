INSERT INTO AUTHOR (AUTHOR_ID, DISPLAY_NAME, PROFILE_IMAGE_URL)
  VALUES (1000, 'Test User', 'http://localhost/sdsdf.jpg');

INSERT INTO POST_GROUP (GROUP_ID, DISPLAY_NAME)
  VALUES (12, 'GROUP 1');

INSERT INTO POST (POST_ID,
                  APPROVED_COMMENT_ID,
                  BOUNTY,
                  CONTENT,
                  CREATED_AT,
                  FLAGS,
                  MEDIAS,
                  TAGS,
                  TITLE,
                  AUTHOR_ID,
                  GROUP_ID)
  VALUES (
        125,
        NULL,
        0,
        'Hello World',
        '2014-04-28 16:00:49.455',
        NULL,
        NULL,
        '["hello"]',
        'Title 1',
        1000,
        12
      ),
      (
        126,
        NULL,
        0,
        'Hello World',
        '2014-04-28 16:00:49.455',
        NULL,
        NULL,
        '["hello"]',
        'Title 1',
        1000,
        12
      ),
      (
        127,
        NULL,
        0,
        'Hello World',
        '2014-04-28 16:00:49.455',
        NULL,
        NULL,
        '["hello"]',
        'Title 1',
        1000,
        12
      ),
      (
        128,
        NULL,
        0,
        'Hello World',
        '2014-04-28 16:00:49.455',
        NULL,
        NULL,
        '["hello"]',
        'Title 1',
        1000,
        12
      )
