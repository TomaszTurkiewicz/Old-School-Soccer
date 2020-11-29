package com.tt.oldschoolsoccer.classes

 data class User (var id:String?,
                    var userName:String = "ANONYMOUS",
                    var easyGame:Game = Game(),
                    var normalGame:Game = Game(),
                    var hardGame:Game = Game(),
                    var multiGame:Game = Game())   {
     constructor():this(null)
}