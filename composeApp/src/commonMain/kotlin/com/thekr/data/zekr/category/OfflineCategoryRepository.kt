package com.thekr.data.zekr.category

import com.thekr.model.Category

class OfflineCategoryRepository(private val categoryDAO: CategoryDAO) : CategoryRepository {
    override suspend fun insert(category: Category): Long = categoryDAO.insert(category)

    override suspend fun insertAll(categoryList: List<Category>) =
        categoryDAO.insertAll(categoryList)

    override fun findById(id: Long) = categoryDAO.findById(id)
    override fun findAll() = categoryDAO.findAll()
    override fun findByParentId(parent: Long) =
        categoryDAO.findByParentId(parent)

    override fun findByRootId(id: Long) = categoryDAO.findByRootId(id)

    override fun findAllExcept(id: Long) = categoryDAO.findAllExcept(id)

    override suspend fun delete(category: Category) = categoryDAO.delete(category)
    override suspend fun update(category: Category) = categoryDAO.update(category)
    override fun isCategoryHasChild(id: Long) = categoryDAO.isCategoryHasChild(id)
}