class SegmentTree {
    fun drive() {
        val input = intArrayOf(-1, 3, 4, 0, 2, 1)
//        val input = intArrayOf(-1, 2, 4, 0)
//        val input = intArrayOf(2, 1, 5, 6, 2, 3)
        val builtTree = buildSegmentTreeRangeMinimumQuery(0 , input.size - 1 , input)
        TreePrinter.print(builtTree)
        val min = minimumInARangeQuery(Range(0,2),builtTree);
        println("min.... $min")
        println()
        val emptyArrayForRepresentation = IntArray(4 * input.size)
        val arrayRepresentation = constructArrayOfSegmentTree(0,builtTree, input , emptyArrayForRepresentation)
        arrayRepresentation.forEach {
            print("$it ,")
        }
        println()
    }

    fun drive2() {
        val input = intArrayOf(-1, 2, 4, 0)
        val builtTree = buildSegmentTreeRangeMinimumQuery(0 , input.size - 1 , input)
        BinaryTreePrintingUtils().print("",builtTree, false)
        TreePrinter.print(builtTree)

        println()
        val emptyArrayForRepresentation = IntArray(7)
        val arrayRepresentation = constructArrayOfSegmentTree(0, builtTree, input, emptyArrayForRepresentation)
        arrayRepresentation.forEach {
            print("$it ,")
        }
        println()


    }

    data class Node(
        var left: Node? = null,
        var right: Node? = null,
        var value : Int = 0,
        var range : Range? = null,
        var indexInArray : Int =  -1
    ) : TreePrinter.PrintableNode {

        override fun getLeft(): TreePrinter.PrintableNode ?{
            return left
        }

        override fun getRight(): TreePrinter.PrintableNode ?{
            return right
        }

        override fun getText(): String {
            return value.toString() + "[${range!!.lower} ${range!!.upper}] + ($indexInArray)"
        }
    }

    data class Range(val lower : Int, val upper: Int)

    private fun buildSegmentTreeRangeMinimumQuery(l : Int, r : Int, input : IntArray) :Node?{
        if(l > r) return null
        if(l == r)
            return Node(null, null, input[l], Range(l, r) ,l)
        val root = Node()
        val mid = (l + r) / 2
        val left = buildSegmentTreeRangeMinimumQuery(l , mid , input)
        val right = buildSegmentTreeRangeMinimumQuery(mid + 1 , r , input)
        root.left = left
        root.right = right
        root.value = Math.min(left?.value ?: Int.MAX_VALUE, right?.value ?: Int.MAX_VALUE)
        root.range = Range(left!!.range!!.lower, right!!.range!!.upper)
        return root
    }

    data class IndexAndMinValue(val index : Int, val value : Int)
    private fun minimumInARangeQuery(query: Range, root: Node?): IndexAndMinValue {
        if (root == null) return IndexAndMinValue(-1, Int.MAX_VALUE)

        if (query.lower <= root.range!!.lower &&
            query.upper >= root.range!!.upper) { //total overlap
            return IndexAndMinValue(root.indexInArray,root.value)
        }
        //no overlap
        if(query.lower > root.range!!.upper || query.upper < root.range!!.lower) {
            return IndexAndMinValue(-1, Int.MAX_VALUE)
        }

        //partial overlap
        val minFromLeft = minimumInARangeQuery(query, root.left)
        val minFromRight = minimumInARangeQuery(query, root.right)
        if(minFromLeft.value < minFromRight.value) {
            return minFromLeft
        } else {
            return minFromRight
        }
    }

    private fun constructArrayOfSegmentTree(
        index : Int,
        root: Node?,
        input: IntArray,
        result: IntArray
    ) : IntArray{
        if(root == null) return result
        result[index] = root.value
        constructArrayOfSegmentTree(2 * index + 1, root.left, input, result)
        constructArrayOfSegmentTree(2 * index + 2, root.right, input, result)
        return result
    }




}
