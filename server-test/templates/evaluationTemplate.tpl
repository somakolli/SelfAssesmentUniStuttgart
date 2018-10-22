#foreach(category in categoryMap)
    <div class="card">
        <div class="card-header">
            <table class="table">
                <tbody>
                    <tr>
                        <td class="category">
                            $category.getName()
                        </td>
                        <td id="$category.getId()" class="bar">
                            <div class="progress"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
#end