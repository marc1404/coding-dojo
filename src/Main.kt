import java.io.File
import kotlin.system.exitProcess

const val pageSize = 10

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No arguments given! Please specify a file to read.")
        exitProcess(1)
    }

    val filePath = args[0]
    val file = File(filePath)

    if (!file.exists()) {
        println("The file $file could not be found!")
        exitProcess(1)
    }

    val lines = file.readLines(Charsets.UTF_8)
    var page = 0
    val totalPages = (lines.size - 1) / pageSize

    printPage(lines, page, pageSize)

    var input: String
    var needle = ""
    var lastSearchedLine = 0

    do {
        println("Next page: <space> | Previous page: b | Go to page: g<page> | Quit: q | Search: / | Next search occurrence: n")
        print("Please enter a command: ")

        input = readLine() as String

        if (input.equals("b", true)) {
            page--

            if (page < 0) {
                page = totalPages
            }

            printPage(lines, page, pageSize)

            continue
        }

        if (input == " ") {
            page++

            if (page > totalPages) {
                page = 0
            }

            printPage(lines, page, pageSize)

            continue
        }

        val isSearch = input.startsWith("/")

        if (isSearch) {
            needle = input.split("/")[1]
            lastSearchedLine = 0
        }

        if (isSearch || input.equals("n", true)) {
            var start = lastSearchedLine + 1

            if (start >= lines.size) {
                start = 0
            }

            val end = lines.size - 1

            for (i in start..end) {
                val line = lines[i]

                if (line.contains(needle, true)) {
                    lastSearchedLine = i
                    val pageOfLine = i / pageSize

                    printPage(lines, pageOfLine, pageSize, needle)

                    break
                }
            }

            continue
        }

        if (input.startsWith("g", true)) {
            val pageString = input.toLowerCase().split("g")[1]
            page = pageString.toInt() - 1

            printPage(lines, page, pageSize)
        }
    } while (input != "q")
}

fun printPage(lines: List<String>, page: Int, pageSize: Int, needle: String? = null) {
    println("\u001Bc")
    println("### Start of page ${page + 1} ##")

    val start = page * pageSize
    var end = start + pageSize

    if (end >= lines.size) {
        end = lines.size - 1
    }

    for (i in start..end) {
        var line = lines[i]

        if (needle != null) {
            line = line.replace(needle, " -> -> -> $needle <- <- <- ", true)
        }

        println(line)
    }

    println("### End of page ${page + 1} ##")
}
