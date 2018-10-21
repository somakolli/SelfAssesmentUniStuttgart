#foreach(category in categoryMap)
    <div class="card">
        <div class="card-header">
            <table class="table">
                <tbody>
                    <tr>
                        <td id="$category-getId()" class="category">
                            $category.getName()
                        </td>
                        <td class="bar">
                            <div class="progress"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
#end