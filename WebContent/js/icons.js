homeIconMouseOver = new Image();
homeIconMouseOver.src = "./images/home_highlight.gif";

helpIconMouseOver = new Image();
helpIconMouseOver.src = "./images/help_highlight.gif";

aboutIconMouseOver = new Image();
aboutIconMouseOver.src = "./images/about_highlight.gif";


homeIconCur = new Image();
homeIconCur.src = "./images/home_cur.gif";

helpIconCur = new Image();
helpIconCur.src = "./images/help_cur.gif";

aboutIconCur = new Image();
aboutIconCur.src = "./images/about_cur.gif";



homeIconMouseOut = new Image();
homeIconMouseOut.src = "./images/home_no_highlight.gif";

helpIconMouseOut = new Image();
helpIconMouseOut.src = "./images/help_no_highlight.gif";

aboutIconMouseOut = new Image();
aboutIconMouseOut.src = "./images/about_no_highlight.gif";

function getCurPage() {
    var curPage = $('body').attr('id');
    // Trim the _id part
    if (curPage != undefined) {
        curPage = curPage.substring(0, curPage.length - 3);
        return curPage;
    }
    return curPage;
}


function hiLite(page) {
    if (getCurPage() === page) {
        curPageHiLite(page);
    } else {
        document.images[page + "Icon"].src = eval(page + "Icon" + "MouseOver" + ".src");
    }
    return true;
}

function unHiLite(page) {
    if (getCurPage() === page) {
        //curPageHiLite(page);
        // Do nothing since now you don't have an alternate highlight
    } else {
        document.images[page + "Icon"].src = eval(page + "Icon" + "MouseOut" + ".src");
    }
    return true;
}

function curPageHiLite(page) {
    document.images[page + "Icon"].src = eval(page + "Icon" + "Cur" + ".src");
    return true;
}