#### 查询所有记录
```python
>>> Question.objects.all()
<QuerySet [<Question: What's up?>]>
>>> Question.objects.count()
1
```
#### 保存记录
```python
>>> from django.utils import timezone
>>> q = Question(question_text="What's new?", pub_date=timezone.now())
>>> q.question_text
What's new?
>>> q.save()
```
#### 条件查询

```python
# 按照字段查询
>>> Question.objects.filter(id=1)
<QuerySet [<Question: What's up?>]>
# 使用富api查询,双下划线来表示关系
>>> Question.objects.filter(question_text__startswith='What')
<QuerySet [<Question: What's up?>]>
# 如果结果不存在filter方法返回空的QuerySet
>>> Question.objects.filter(id=2)
<QuerySet []>

# 得到今年的所有问题
>>> from django.utils import timezone
>>> current_year = timezone.now().year
>>> Question.objects.get(pub_date__year=current_year)
# 使用get方法，如果记录不存在会抛出异常
>>> Question.objects.get(id=2)
Traceback (most recent call last):
    ...
DoesNotExist: Question matching query does not exist.
```
#### 关联查询

```python
# 使用pk或者id是一样的都是默认主键
>>> q = Question.objects.get(pk=1)
# 使用关联关系一对多的表名加_set来处理关联的记录
>>> q.choice_set.all()
<QuerySet []>
# Create方法有两个作用，创建了一个实例，并且insert到了数据库中
>>> q.choice_set.create(choice_text='Not much', votes=0)
<Choice: Not much>
>>> q.choice_set.create(choice_text='The sky', votes=0)
<Choice: The sky>
>>> c = q.choice_set.create(choice_text='Just hacking again', votes=0)

# Choice 对象多对一通过富api查找所属的Question对象.
>>> c.question
<Question: What's up?>
# 通过一个Question对象，找到它所有的choice
>>> q.choice_set.all()
<QuerySet [<Choice: Not much>, <Choice: The sky>, <Choice: Just hacking again>]>
>>> q.choice_set.count()
3
# 使用富api查询,双下划线来表示关系，这个富api可以按照你想要的方式使用。
>>> Choice.objects.filter(question__pub_date__year=current_year)
<QuerySet [<Choice: Not much>, <Choice: The sky>, <Choice: Just hacking again>]>
# 使用delete方法可以删除一个记录。
>>> c = q.choice_set.filter(choice_text__startswith='Just hacking')
>>> c.delete()
```