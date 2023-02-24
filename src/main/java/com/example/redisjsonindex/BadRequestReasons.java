package com.example.redisjsonindex;

/**
 * @author Bruce.Yang
 * @date 22/02/23 -14:18
 */
public enum BadRequestReasons {
    DUPLICATED,
    CONFLICT,
    RESERVED,
    UNAUTHORIZED,
    MYSQL_UNKNOWN,
    MYSQL_INTEGRITYCONSTRAINT,
    LICENSE_UNKNOWN,
    LICENSE_UNINSTALLED,
    LICENSE_ISEMPTY,
    LICENSE_FORMAT_ERROR,
    LICENSE_ILLEGAL,
    LICENSE_EXPIRED,
    LICENSE_MAC_NOT_MATCHED,
    LICENSE_WEIGHT_NOT_MATCHED,
    LICENSE_MISMACTCH_CMS_COUNT,
    LICENSE_MISMACTCH_ROOM_COUNT,
    SERIALIZE_CONVERT_EXCEPTION,
    CMS_SERVER_ERROR,
    NO_CMS_BROKER,
    COSPACE_URI_CONFLICT,
    COSPACE_CALLID_CONFLICT,
    INDEX_RANGE_INVALID,
    INDEX_RANGE_TOO_GREAT,
    NOSUCH_PARAMETERSET,
    FAILED,
    DURATION_ENDED,
    CONFERENCE_PROTECTED,
    CMMANAMES_IS_NULL,
    SAVE_BG_IMAGE_FAILED,
    SAVE_LOGO_IMAGE_FAILED,
    MANAGE_WEMEET_USER_FAILED;

}
