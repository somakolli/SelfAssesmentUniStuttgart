function saveQuestionState(input)
{
    if(input.checked)
    {
        location.hash += input.id + ":" + input.value + "#";
    }

}
