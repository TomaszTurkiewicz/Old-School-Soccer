package com.tt.oldschoolsoccer.classes

import com.tt.oldschoolsoccer.database.PointOnField

/**
 * holding info about two points
 * before move and after
 */
class PointsAfterMove(
        var beforeMovePoint:PointOnField = PointOnField(),
        var afterMovePoint:PointOnField = PointOnField()
)