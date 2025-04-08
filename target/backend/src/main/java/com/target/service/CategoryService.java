package com.target.service;

import com.target.dto.CategoryDTO;
import com.target.model.Category;
import com.target.repository.CategoryRepository;
import com.target.exception.ResourceNotFoundException;
import com.target.exception.DuplicateResourceException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setActive(categoryDTO.isActive());
        
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }
}