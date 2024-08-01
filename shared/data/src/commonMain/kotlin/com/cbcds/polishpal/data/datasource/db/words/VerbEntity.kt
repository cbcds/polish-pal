package com.cbcds.polishpal.data.datasource.db.words

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cbcds.polishpal.data.model.words.Aspect

@Suppress("ConstructorParameterNaming")
@Entity(tableName = "verbs")
internal data class VerbEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "inf") val infinitive: String,
    @ColumnInfo(name = "dkn") val aspect: Aspect,
    @ColumnInfo(name = "ozn_trz/przy_lp_1os") val ozn_trz_przy_lp_1os: String,
    @ColumnInfo(name = "ozn_trz/przy_lp_2os") val ozn_trz_przy_lp_2os: String,
    @ColumnInfo(name = "ozn_trz/przy_lp_3os") val ozn_trz_przy_lp_3os: String,
    @ColumnInfo(name = "ozn_trz/przy_lm_1os") val ozn_trz_przy_lm_1os: String,
    @ColumnInfo(name = "ozn_trz/przy_lm_2os") val ozn_trz_przy_lm_2os: String,
    @ColumnInfo(name = "ozn_trz/przy_lm_3os") val ozn_trz_przy_lm_3os: String,
    @ColumnInfo(name = "ozn_przesz_lp_1os_rm") val ozn_przesz_lp_1os_rm: String,
    @ColumnInfo(name = "ozn_przesz_lp_1os_rż") val ozn_przesz_lp_1os_rz: String,
    @ColumnInfo(name = "ozn_przesz_lp_2os_rm") val ozn_przesz_lp_2os_rm: String,
    @ColumnInfo(name = "ozn_przesz_lp_2os_rż") val ozn_przesz_lp_2os_rz: String,
    @ColumnInfo(name = "ozn_przesz_lp_3os_rm") val ozn_przesz_lp_3os_rm: String,
    @ColumnInfo(name = "ozn_przesz_lp_3os_rż") val ozn_przesz_lp_3os_rz: String,
    @ColumnInfo(name = "ozn_przesz_lp_3os_rn") val ozn_przesz_lp_3os_rn: String,
    @ColumnInfo(name = "ozn_przesz_lm_1os_rmo") val ozn_przesz_lm_1os_rmo: String,
    @ColumnInfo(name = "ozn_przesz_lm_1os_rnmo") val ozn_przesz_lm_1os_rnmo: String,
    @ColumnInfo(name = "ozn_przesz_lm_2os_rmo") val ozn_przesz_lm_2os_rmo: String,
    @ColumnInfo(name = "ozn_przesz_lm_2os_rnmo") val ozn_przesz_lm_2os_rnmo: String,
    @ColumnInfo(name = "ozn_przesz_lm_3os_rmo") val ozn_przesz_lm_3os_rmo: String,
    @ColumnInfo(name = "ozn_przesz_lm_3os_rnmo") val ozn_przesz_lm_3os_rnmo: String,
    @ColumnInfo(name = "rozk_lp_2os") val rozk_lp_2os: String,
    @ColumnInfo(name = "rozk_lm_1os") val rozk_lm_1os: String,
    @ColumnInfo(name = "rozk_lm_2os") val rozk_lm_2os: String,
    @ColumnInfo(name = "def") val definition: String,
)
