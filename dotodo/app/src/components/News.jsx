export default function News() {
    return (
        <div className="content news">
            <div>
                <h3>Latest news and updates</h3>
                <ul>
                    <li className="news-article">
                        <h4><a href="blog-single-with-image.html">New Feature Alert: Notes and Checklists Now Available</a></h4>
                        <div className="article-info">
                            <span>Posted on <a href="#">19 Dec 2023</a> in <a href="#">Features</a></span>
                            <span><a className="comments" href="#">   1 Comments</a></span>
                        </div>
                        <p>
                        Our task management app just got better! We're thrilled to introduce two new features: Notes and Checklists. 
                        These additions are crafted to provide you with more flexibility and control over your daily tasks. 
                        The Notes feature allows for quick jotting down of ideas and thoughts, while Checklists offer a structured 
                        format for task completion. These features are designed to cater to the diverse needs of our users, making 
                        task management more efficient and personalized.
                        </p>
                        <a className="read-more" href="blog-single-with-image.html">Read More</a>
                    </li>
                    <li className="news-article">
                        <h4><a href="blog-single-with-video.html">Launch of Revolutionary Task Management App</a></h4>
                        <div className="article-info">
                            <span>Posted on <a href="#">17 Dec 2023</a> in <a href="#">Maintenance</a></span>
                            <span><a className="comments" href="#">  0 Comments</a></span>
                        </div>
                        <p>
                            We are excited to announce the launch of our innovative task management app, designed to streamline 
                            your daily tasks and boost productivity. Our platform offers a user-friendly interface, making it 
                            easier for you to organize and prioritize your to-do lists, notes, and schedules. With the goal of 
                            enhancing personal and professional efficiency, this app is the result of extensive research and 
                            commitment to improving time management for everyone.
                        </p>
                        <a className="read-more" href="blog-single-with-video.html">Read More</a>
                    </li>
                </ul>
                <div className="paging">
                    <ul>
                        <li className="selected">
                            <a href="#">1</a>
                        </li>
                        <li>
                            <a href="#">2</a>
                        </li>
                        <li>
                            <a href="#">{'>'}</a>
                        </li>
                    </ul>
                    <span>Page 1 of 2</span>
                </div>
            </div>
        </div>
    );
}