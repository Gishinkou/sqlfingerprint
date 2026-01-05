package main

import (
	"fmt"

	"github.com/percona/go-mysql/query"
)

//TIP <p>To run your code, right-click the code and select <b>Run</b>.</p> <p>Alternatively, click
// the <icon src="AllIcons.Actions.Execute"/> icon in the gutter and select the <b>Run</b> menu item from here.</p>

func main() {
	//TIP <p>Press <shortcut actionId="ShowIntentionActions"/> when your caret is at the underlined text
	// to see how GoLand suggests fixing the warning.</p><p>Alternatively, if available, click the lightbulb to view possible fixes.</p>
	rawSql := "select * from users where id = 1"
	fmt.Println("rawSql:" + rawSql)

	// 默认情况下，Fingerprint 不会处理表名中的数字后缀
	parsed := query.Fingerprint(rawSql)
	fmt.Println("Default Fingerprint:", parsed)

	// 设置全局变量 ReplaceNumbersInWords 为 true，启用替换标识符中数字的功能
	// 注意：这是一个全局开关，会影响所有 Fingerprint 调用
	query.ReplaceNumbersInWords = true
	rawSql = "select app_name, user_name from users_00 where id = 1;"
	fmt.Println("rawSql:" + rawSql)
	parsed = query.Fingerprint(rawSql)
	fmt.Println("Fingerprint with ReplaceNumbersInWords=true:", parsed)
}
